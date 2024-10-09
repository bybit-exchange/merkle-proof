package com.bybit.merkle.generic;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.EnumMap;

@JsonDeserialize(using = Balance40V3.BalanceDeserializer.class)
public class Balance40V3 extends Balance<Asset40V3> {

    public Balance40V3() {
        balances = new EnumMap<>(Asset40V3.class);
    }

    @Override
    public boolean validate() {
        try {
            for (Asset40V3 asset : Asset40V3.getBalances()) {
                new BigDecimal(balances.get(asset));
            }
        } catch (NullPointerException | NumberFormatException e) {
            return false;
        }
        return true;
    }

    @Override
    public Balance<Asset40V3> add(Balance<Asset40V3> other) {
        Balance40V3 res = new Balance40V3();
        for (Asset40V3 asset : Asset40V3.getBalances()) {
            BigDecimal thisValue = new BigDecimal(this.balances.get(asset));
            BigDecimal otherValue = new BigDecimal(other.balances.get(asset));
            BigDecimal sum = thisValue.add(otherValue).setScale(asset.getScale(), asset.getMode());
            res.balances.put(asset, sum.toPlainString());
        }
        return res;
    }

    public static class BalanceDeserializer extends JsonDeserializer<Balance40V3> {

        @Override
        public Balance40V3 deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException {
            Balance40V3 balance = new Balance40V3();
            EnumMap<Asset40V3, String> balanceMap = balance.getBalances();

            JsonToken currentToken = parser.nextToken();
            while (currentToken != JsonToken.END_OBJECT) {
                Asset40V3 asset = Asset40V3.valueOf(parser.getCurrentName());
                parser.nextToken();
                balanceMap.put(asset, parser.getText());
                currentToken = parser.nextToken();
            }

            return balance;
        }
    }
}
