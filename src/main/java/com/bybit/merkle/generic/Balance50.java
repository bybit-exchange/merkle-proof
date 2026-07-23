package com.bybit.merkle.generic;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.EnumMap;

@JsonDeserialize(using = Balance50.BalanceDeserializer.class)
public class Balance50 extends Balance<Asset50> {

    public Balance50() {
        balances = new EnumMap<>(Asset50.class);
    }

    @Override
    public boolean validate() {
        try {
            for (Asset50 asset : Asset50.getBalances()) {
                new BigDecimal(balances.get(asset));
            }
        } catch (NullPointerException | NumberFormatException e) {
            return false;
        }
        return true;
    }

    @Override
    public Balance<Asset50> add(Balance<Asset50> other) {
        Balance50 res = new Balance50();
        for (Asset50 asset : Asset50.getBalances()) {
            BigDecimal thisValue = new BigDecimal(this.balances.get(asset));
            BigDecimal otherValue = new BigDecimal(other.balances.get(asset));
            BigDecimal sum = thisValue.add(otherValue).setScale(asset.getScale(), asset.getMode());
            res.balances.put(asset, sum.toPlainString());
        }
        return res;
    }

    public static class BalanceDeserializer extends JsonDeserializer<Balance50> {

        @Override
        public Balance50 deserialize(
                JsonParser parser, DeserializationContext deserializationContext)
                throws IOException {
            Balance50 balance = new Balance50();
            EnumMap<Asset50, String> balanceMap = balance.getBalances();

            JsonToken currentToken = parser.nextToken();
            while (currentToken != JsonToken.END_OBJECT) {
                Asset50 asset = Asset50.valueOf(parser.getCurrentName());
                parser.nextToken();
                balanceMap.put(asset, parser.getText());
                currentToken = parser.nextToken();
            }

            return balance;
        }
    }
}
