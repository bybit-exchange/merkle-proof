package com.bybit.merkle.generic;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.EnumMap;

@JsonDeserialize(using = Balance40V2.BalanceDeserializer.class)
public class Balance40V2 extends Balance<Asset40V2> {

    public Balance40V2() {
        balances = new EnumMap<>(Asset40V2.class);
    }

    @Override
    public boolean validate() {
        try {
            for (Asset40V2 asset : Asset40V2.getBalances()) {
                new BigDecimal(balances.get(asset));
            }
        } catch (NullPointerException | NumberFormatException e) {
            return false;
        }
        return true;
    }

    @Override
    public Balance<Asset40V2> add(Balance<Asset40V2> other) {
        Balance40V2 res = new Balance40V2();
        for (Asset40V2 asset : Asset40V2.getBalances()) {
            BigDecimal thisValue = new BigDecimal(this.balances.get(asset));
            BigDecimal otherValue = new BigDecimal(other.balances.get(asset));
            BigDecimal sum = thisValue.add(otherValue).setScale(asset.getScale(), asset.getMode());
            res.balances.put(asset, sum.toPlainString());
        }
        return res;
    }

    public static class BalanceDeserializer extends JsonDeserializer<Balance40V2> {

        @Override
        public Balance40V2 deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException {
            Balance40V2 balance = new Balance40V2();
            EnumMap<Asset40V2, String> balanceMap = balance.getBalances();

            JsonToken currentToken = parser.nextToken();
            while (currentToken != JsonToken.END_OBJECT) {
                Asset40V2 asset = Asset40V2.valueOf(parser.getCurrentName());
                parser.nextToken();
                balanceMap.put(asset, parser.getText());
                currentToken = parser.nextToken();
            }

            return balance;
        }
    }
}
