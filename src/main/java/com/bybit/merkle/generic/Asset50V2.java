package com.bybit.merkle.generic;

import lombok.Getter;

import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum Asset50V2 {
    AAVE((short) 1, 8, RoundingMode.DOWN, true),
    APEX((short) 2, 8, RoundingMode.DOWN, true),
    APT((short) 3, 8, RoundingMode.DOWN, true),
    ARB((short) 4, 8, RoundingMode.DOWN, true),
    ASTER((short) 5, 8, RoundingMode.DOWN, true),
    ATOM((short) 6, 8, RoundingMode.DOWN, true),
    AVAX((short) 7, 8, RoundingMode.DOWN, true),
    BTC((short) 8, 8, RoundingMode.DOWN, true),
    CAP((short) 9, 8, RoundingMode.DOWN, true),
    COMP((short) 10, 8, RoundingMode.DOWN, true),
    CRV((short) 11, 8, RoundingMode.DOWN, true),
    DOGE((short) 12, 8, RoundingMode.DOWN, true),
    DOT((short) 13, 8, RoundingMode.DOWN, true),
    DYDX((short) 14, 8, RoundingMode.DOWN, true),
    ENA((short) 15, 8, RoundingMode.DOWN, true),
    ETH((short) 16, 8, RoundingMode.DOWN, true),
    FET((short) 17, 8, RoundingMode.DOWN, true),
    GALA((short) 18, 8, RoundingMode.DOWN, true),
    GRAM((short) 19, 8, RoundingMode.DOWN, true),
    HYPE((short) 20, 8, RoundingMode.DOWN, true),
    LDO((short) 21, 8, RoundingMode.DOWN, true),
    LINK((short) 22, 8, RoundingMode.DOWN, true),
    LIT((short) 23, 8, RoundingMode.DOWN, true),
    LTC((short) 24, 8, RoundingMode.DOWN, true),
    MNT((short) 25, 8, RoundingMode.DOWN, true),
    ONDO((short) 26, 8, RoundingMode.DOWN, true),
    OP((short) 27, 8, RoundingMode.DOWN, true),
    PENGU((short) 28, 8, RoundingMode.DOWN, true),
    PEPE((short) 29, 8, RoundingMode.DOWN, true),
    POL((short) 30, 8, RoundingMode.DOWN, true),
    PUMP((short) 31, 8, RoundingMode.DOWN, true),
    RENDER((short) 32, 8, RoundingMode.DOWN, true),
    S((short) 33, 8, RoundingMode.DOWN, true),
    SHIB((short) 34, 8, RoundingMode.DOWN, true),
    SOL((short) 35, 8, RoundingMode.DOWN, true),
    SPX((short) 36, 8, RoundingMode.DOWN, true),
    STABLE((short) 37, 8, RoundingMode.DOWN, true),
    SUI((short) 38, 8, RoundingMode.DOWN, true),
    UNI((short) 39, 8, RoundingMode.DOWN, true),
    USDC((short) 40, 8, RoundingMode.DOWN, true),
    USDE((short) 41, 8, RoundingMode.DOWN, true),
    USD1((short) 42, 8, RoundingMode.DOWN, true),
    USDT((short) 43, 8, RoundingMode.DOWN, true),
    USDTB((short) 44, 8, RoundingMode.DOWN, true),
    WLD((short) 45, 8, RoundingMode.DOWN, true),
    XAUT((short) 46, 8, RoundingMode.DOWN, true),
    XPL((short) 47, 8, RoundingMode.DOWN, true),
    XRP((short) 48, 8, RoundingMode.DOWN, true),
    ZEREBRO((short) 49, 8, RoundingMode.DOWN, true),
    ZRO((short) 50, 8, RoundingMode.DOWN, true);

    private final short id;
    private final int scale;
    private final RoundingMode mode;
    private final boolean online;

    Asset50V2(short id, int scale, RoundingMode mode, boolean online) {
        this.id = id;
        this.scale = scale;
        this.mode = mode;
        this.online = online;
    }

    public static List<Asset50V2> getBalances() {
        return Arrays.stream(values()).filter(Asset50V2::isOnline).collect(Collectors.toList());
    }
}
