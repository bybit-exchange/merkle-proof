package com.bybit.merkle.generic;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.EnumMap;

@JsonDeserialize(using = BalanceMnt20.BalanceDeserializer.class)
public class BalanceMnt20 extends Balance<AssetMnt20> {

    public BalanceMnt20() {
        balances = new EnumMap<>(AssetMnt20.class);
    }

    @Override
    public boolean validate() {
        try {
            for (AssetMnt20 asset : AssetMnt20.getBalances()) {
                new BigDecimal(balances.get(asset));
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public Balance<AssetMnt20> add(Balance<AssetMnt20> other) {
        BalanceMnt20 res = new BalanceMnt20();
        for (AssetMnt20 asset : AssetMnt20.getBalances()) {
            BigDecimal thisValue = new BigDecimal(this.balances.get(asset));
            BigDecimal otherValue = new BigDecimal(other.balances.get(asset));
            BigDecimal sum = thisValue.add(otherValue).setScale(asset.getScale(), asset.getMode());
            res.balances.put(asset, sum.toPlainString());
        }
        return res;
    }

    public static class BalanceDeserializer extends JsonDeserializer<BalanceMnt20> {

        @Override
        public BalanceMnt20 deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException {
            BalanceMnt20 balance = new BalanceMnt20();
            EnumMap<AssetMnt20, String> balanceMap = balance.getBalances();

            JsonToken currentToken = parser.nextToken();
            while (currentToken != JsonToken.END_OBJECT) {
                String assetName = parser.getCurrentName();
                AssetMnt20 asset = AssetMnt20.valueOf(assetName);
                parser.nextToken();
                String value = parser.getText();
                balanceMap.put(asset, value);
                currentToken = parser.nextToken();
            }

            return balance;
        }
    }
}
