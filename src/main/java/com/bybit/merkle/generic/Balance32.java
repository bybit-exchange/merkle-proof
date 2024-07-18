package com.bybit.merkle.generic;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.EnumMap;

@JsonDeserialize(using = Balance32.BalanceDeserializer.class)
public class Balance32 extends Balance<Asset32> {

    public Balance32() {
        balances = new EnumMap<>(Asset32.class);
    }

    @Override
    public boolean validate() {
        try {
            for (Asset32 asset : Asset32.getBalances()) {
                new BigDecimal(balances.get(asset));
            }
        } catch (NullPointerException | NumberFormatException e) {
            return false;
        }
        return true;
    }

    @Override
    public Balance<Asset32> add(Balance<Asset32> other) {
        Balance32 res = new Balance32();
        for (Asset32 asset : Asset32.getBalances()) {
            BigDecimal thisValue = new BigDecimal(this.balances.get(asset));
            BigDecimal otherValue = new BigDecimal(other.balances.get(asset));
            BigDecimal sum = thisValue.add(otherValue).setScale(asset.getScale(), asset.getMode());
            res.balances.put(asset, sum.toPlainString());
        }
        return res;
    }

    public static class BalanceDeserializer extends JsonDeserializer<Balance32> {

        @Override
        public Balance32 deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException {
            Balance32 balance = new Balance32();
            EnumMap<Asset32, String> balanceMap = balance.getBalances();

            JsonToken currentToken = parser.nextToken();
            while (currentToken != JsonToken.END_OBJECT) {
                String assetName = parser.getCurrentName();
                Asset32 asset = Asset32.valueOf(assetName);
                parser.nextToken();
                String value = parser.getText();
                balanceMap.put(asset, value);
                currentToken = parser.nextToken();
            }

            return balance;
        }
    }
}
