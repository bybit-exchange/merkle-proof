package com.bybit.merkle.generic;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.EnumMap;

@JsonDeserialize(using = Balance40.BalanceDeserializer.class)
public class Balance40 extends Balance<Asset40> {

    public Balance40() {
        balances = new EnumMap<>(Asset40.class);
    }

    @Override
    public boolean validate() {
        try {
            for (Asset40 asset : Asset40.getBalances()) {
                new BigDecimal(balances.get(asset));
            }
        } catch (NullPointerException | NumberFormatException e) {
            return false;
        }
        return true;
    }

    @Override
    public Balance<Asset40> add(Balance<Asset40> other) {
        Balance40 res = new Balance40();
        for (Asset40 asset : Asset40.getBalances()) {
            BigDecimal thisValue = new BigDecimal(this.balances.get(asset));
            BigDecimal otherValue = new BigDecimal(other.balances.get(asset));
            BigDecimal sum = thisValue.add(otherValue).setScale(asset.getScale(), asset.getMode());
            res.balances.put(asset, sum.toPlainString());
        }
        return res;
    }

    public static class BalanceDeserializer extends JsonDeserializer<Balance40> {

        @Override
        public Balance40 deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException {
            Balance40 balance = new Balance40();
            EnumMap<Asset40, String> balanceMap = balance.getBalances();

            JsonToken currentToken = parser.nextToken();
            while (currentToken != JsonToken.END_OBJECT) {
                String assetName = parser.getCurrentName();
                Asset40 asset = Asset40.valueOf(assetName);
                parser.nextToken();
                String value = parser.getText();
                balanceMap.put(asset, value);
                currentToken = parser.nextToken();
            }

            return balance;
        }
    }
}
