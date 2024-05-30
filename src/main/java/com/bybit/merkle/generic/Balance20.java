package com.bybit.merkle.generic;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.EnumMap;

@JsonDeserialize(using = Balance20.BalanceDeserializer.class)
public class Balance20 extends Balance<Asset20> {

    public Balance20() {
        balances = new EnumMap<>(Asset20.class);
    }

    @Override
    public boolean validate() {
        try {
            for (Asset20 asset : Asset20.getBalances()) {
                new BigDecimal(balances.get(asset));
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public Balance<Asset20> add(Balance<Asset20> other) {
        Balance20 res = new Balance20();
        for (Asset20 asset : Asset20.getBalances()) {
            BigDecimal thisValue = new BigDecimal(this.balances.get(asset));
            BigDecimal otherValue = new BigDecimal(other.balances.get(asset));
            BigDecimal sum = thisValue.add(otherValue).setScale(asset.getScale(), asset.getMode());
            res.balances.put(asset, sum.toPlainString());
        }
        return res;
    }

    public static class BalanceDeserializer extends JsonDeserializer<Balance20> {

        @Override
        public Balance20 deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException {
            Balance20 balance = new Balance20();
            EnumMap<Asset20, String> balanceMap = balance.getBalances();

            JsonToken currentToken = parser.nextToken();
            while (currentToken != JsonToken.END_OBJECT) {
                String assetName = parser.getCurrentName();
                Asset20 asset = Asset20.valueOf(assetName);
                parser.nextToken();
                String value = parser.getText();
                balanceMap.put(asset, value);
                currentToken = parser.nextToken();
            }

            return balance;
        }
    }
}
