package com.bybit.merkle.generic;

import lombok.Data;

import java.util.EnumMap;

@Data
public abstract class Balance<T extends Enum<T>> {
    protected EnumMap<T, String> balances;

    public abstract boolean validate();

    public abstract Balance<T> add(Balance<T> balance);

    public String concatBalances() {
        return String.join("", balances.values());
    }
}
