package com.bybit.merkle.generic;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.EnumMap;

@JsonDeserialize(using = Balance50V2.BalanceDeserializer.class)
public class Balance50V2 extends Balance<Asset50V2> {

    public Balance50V2() {
        balances = new EnumMap<>(Asset50V2.class);
    }

    @Override
    public boolean validate() {
        try {
            for (Asset50V2 asset : Asset50V2.getBalances()) {
                new BigDecimal(balances.get(asset));
            }
        } catch (NullPointerException | NumberFormatException e) {
            return false;
        }
        return true;
    }

    @Override
    public Balance<Asset50V2> add(Balance<Asset50V2> other) {
        Balance50V2 res = new Balance50V2();
        for (Asset50V2 asset : Asset50V2.getBalances()) {
            BigDecimal thisValue = new BigDecimal(this.balances.get(asset));
            BigDecimal otherValue = new BigDecimal(other.balances.get(asset));
            BigDecimal sum = thisValue.add(otherValue).setScale(asset.getScale(), asset.getMode());
            res.balances.put(asset, sum.toPlainString());
        }
        return res;
    }

    public static class BalanceDeserializer extends JsonDeserializer<Balance50V2> {

        @Override
        public Balance50V2 deserialize(
                JsonParser parser, DeserializationContext deserializationContext)
                throws IOException {
            Balance50V2 balance = new Balance50V2();
            EnumMap<Asset50V2, String> balanceMap = balance.getBalances();

            JsonToken currentToken = parser.nextToken();
            while (currentToken != JsonToken.END_OBJECT) {
                Asset50V2 asset = Asset50V2.valueOf(parser.getCurrentName());
                parser.nextToken();
                balanceMap.put(asset, parser.getText());
                currentToken = parser.nextToken();
            }

            return balance;
        }
    }
}
