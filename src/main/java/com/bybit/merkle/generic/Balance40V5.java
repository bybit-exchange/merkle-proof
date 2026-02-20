package com.bybit.merkle.generic;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.EnumMap;

@JsonDeserialize(using = Balance40V5.BalanceDeserializer.class)
public class Balance40V5 extends Balance<Asset40V5> {

    public Balance40V5() {
        balances = new EnumMap<>(Asset40V5.class);
    }

    @Override
    public boolean validate() {
        try {
            for (Asset40V5 asset : Asset40V5.getBalances()) {
                new BigDecimal(balances.get(asset));
            }
        } catch (NullPointerException | NumberFormatException e) {
            return false;
        }
        return true;
    }

    @Override
    public Balance<Asset40V5> add(Balance<Asset40V5> other) {
        Balance40V5 res = new Balance40V5();
        for (Asset40V5 asset : Asset40V5.getBalances()) {
            BigDecimal thisValue = new BigDecimal(this.balances.get(asset));
            BigDecimal otherValue = new BigDecimal(other.balances.get(asset));
            BigDecimal sum = thisValue.add(otherValue).setScale(asset.getScale(), asset.getMode());
            res.balances.put(asset, sum.toPlainString());
        }
        return res;
    }

    public static class BalanceDeserializer extends JsonDeserializer<Balance40V5> {

        @Override
        public Balance40V5 deserialize(
                JsonParser parser, DeserializationContext deserializationContext)
                throws IOException {
            Balance40V5 balance = new Balance40V5();
            EnumMap<Asset40V5, String> balanceMap = balance.getBalances();

            JsonToken currentToken = parser.nextToken();
            while (currentToken != JsonToken.END_OBJECT) {
                Asset40V5 asset = Asset40V5.valueOf(parser.getCurrentName());
                parser.nextToken();
                balanceMap.put(asset, parser.getText());
                currentToken = parser.nextToken();
            }

            return balance;
        }
    }
}
