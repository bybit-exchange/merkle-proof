package com.bybit.merkle.generic;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.EnumMap;

@JsonDeserialize(using = Balance40V4.BalanceDeserializer.class)
public class Balance40V4 extends Balance<Asset40V4> {

    public Balance40V4() {
        balances = new EnumMap<>(Asset40V4.class);
    }

    @Override
    public boolean validate() {
        try {
            for (Asset40V4 asset : Asset40V4.getBalances()) {
                new BigDecimal(balances.get(asset));
            }
        } catch (NullPointerException | NumberFormatException e) {
            return false;
        }
        return true;
    }

    @Override
    public Balance<Asset40V4> add(Balance<Asset40V4> other) {
        Balance40V4 res = new Balance40V4();
        for (Asset40V4 asset : Asset40V4.getBalances()) {
            BigDecimal thisValue = new BigDecimal(this.balances.get(asset));
            BigDecimal otherValue = new BigDecimal(other.balances.get(asset));
            BigDecimal sum = thisValue.add(otherValue).setScale(asset.getScale(), asset.getMode());
            res.balances.put(asset, sum.toPlainString());
        }
        return res;
    }

    public static class BalanceDeserializer extends JsonDeserializer<Balance40V4> {

        @Override
        public Balance40V4 deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException {
            Balance40V4 balance = new Balance40V4();
            EnumMap<Asset40V4, String> balanceMap = balance.getBalances();

            JsonToken currentToken = parser.nextToken();
            while (currentToken != JsonToken.END_OBJECT) {
                Asset40V4 asset = Asset40V4.valueOf(parser.getCurrentName());
                parser.nextToken();
                balanceMap.put(asset, parser.getText());
                currentToken = parser.nextToken();
            }

            return balance;
        }
    }
}
