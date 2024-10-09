package com.bybit.merkle.generic;

import lombok.Getter;

import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum Asset40V3 {
    AGLA((short) 1, 8, RoundingMode.DOWN, true),
    APT((short) 2, 8, RoundingMode.DOWN, true),
    ATOM((short) 3, 8, RoundingMode.DOWN, true),
    AVAX((short) 4, 8, RoundingMode.DOWN, true),
    // change bit to mnt
    MNT((short) 5, 8, RoundingMode.DOWN, true),
    BTC((short) 6, 8, RoundingMode.DOWN, true),
    BUSD((short) 7, 8, RoundingMode.DOWN, false),
    // from release 20240808
    // DAI((short) 8, 8, RoundingMode.DOWN, true),
    DOGE((short) 9, 8, RoundingMode.DOWN, true),
    DOT((short) 10, 8, RoundingMode.DOWN, true),
    DYDX((short) 11, 8, RoundingMode.DOWN, true),
    EOS((short) 12, 8, RoundingMode.DOWN, true),
    ETH((short) 13, 8, RoundingMode.DOWN, true),
    FTM((short) 14, 8, RoundingMode.DOWN, true),
    LTC((short) 15, 8, RoundingMode.DOWN, true),
    // from release 20241010
    // change MATIC to POL
    POL((short) 16, 8, RoundingMode.DOWN, true),
    SOL((short) 17, 8, RoundingMode.DOWN, true),
    USDC((short) 18, 8, RoundingMode.DOWN, true),
    USDT((short) 19, 8, RoundingMode.DOWN, true),
    XRP((short) 20, 8, RoundingMode.DOWN, true),
    SHIB((short) 21, 8, RoundingMode.DOWN, true),
    OP((short) 22, 8, RoundingMode.DOWN, true),
    SAND((short) 23, 8, RoundingMode.DOWN, true),
    LDO((short) 24, 8, RoundingMode.DOWN, true),
    LINK((short) 25, 8, RoundingMode.DOWN, true),
    COMP((short) 26, 8, RoundingMode.DOWN, true),
    UNI((short) 27, 8, RoundingMode.DOWN, true),
    MANA((short) 28, 8, RoundingMode.DOWN, true),
    CRV((short) 29, 8, RoundingMode.DOWN, true),
    BLUR((short) 30, 8, RoundingMode.DOWN, true),
    SUSHI((short) 31, 8, RoundingMode.DOWN, true),
    PEPE((short) 32, 8, RoundingMode.DOWN, true),
    // from release 20240403
    WLD((short) 33, 8, RoundingMode.DOWN, true),
    BEAM((short) 34, 8, RoundingMode.DOWN, true),
    AGI((short) 35, 8, RoundingMode.DOWN, true),
    FET((short) 36, 8, RoundingMode.DOWN, true),
    IMX((short) 37, 8, RoundingMode.DOWN, true),
    // from release 20240808
    // change RNDR to RENDER
    RENDER((short) 38, 8, RoundingMode.DOWN, true),
    GALA((short) 39, 8, RoundingMode.DOWN, true),
    APEX((short) 40, 8, RoundingMode.DOWN, true),
    SHRAP((short) 41, 8, RoundingMode.DOWN, true),
    // from release 20240808
    USDE((short) 42, 8, RoundingMode.DOWN, true);

    private final short id;
    private final int scale;
    private final RoundingMode mode;
    private final boolean online;

    Asset40V3(short id, int scale, RoundingMode mode, boolean online) {
        this.id = id;
        this.scale = scale;
        this.mode = mode;
        this.online = online;
    }

    public static List<Asset40V3> getBalances() {
        return Arrays.stream(values()).filter(Asset40V3::isOnline).collect(Collectors.toList());
    }
}
