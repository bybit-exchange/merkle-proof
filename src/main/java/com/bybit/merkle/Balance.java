package com.bybit.merkle;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Balance {

    private String BTC;
    private String ETH;
    private String USDT;
    private String USDC;

    public String getBTC() {
        return BTC;
    }

    public void setBTC(String BTC) {
        this.BTC = BTC;
    }

    public String getETH() {
        return ETH;
    }

    public void setETH(String ETH) {
        this.ETH = ETH;
    }

    public String getUSDT() {
        return USDT;
    }

    public void setUSDT(String USDT) {
        this.USDT = USDT;
    }

    public String getUSDC() {
        return USDC;
    }

    public void setUSDC(String USDC) {
        this.USDC = USDC;
    }

    public boolean validate(){
        try {
            BigDecimal btcDecimal = new BigDecimal(this.BTC);
            BigDecimal ethDecimal = new BigDecimal(this.ETH);
            BigDecimal usdtDecimal = new BigDecimal(this.USDT);
            BigDecimal usdcDecimal = new BigDecimal(this.USDC);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public Balance add(Balance balance){
        try {
            BigDecimal balanceBTC = new BigDecimal(balance.BTC);
            BigDecimal balanceETH = new BigDecimal(balance.ETH);
            BigDecimal balanceUSDT = new BigDecimal(balance.USDT);
            BigDecimal balanceUSDC = new BigDecimal(balance.USDC);
            BigDecimal btcDecimal = new BigDecimal(this.BTC);
            BigDecimal ethDecimal = new BigDecimal(this.ETH);
            BigDecimal usdtDecimal = new BigDecimal(this.USDT);
            BigDecimal usdcDecimal = new BigDecimal(this.USDC);
            Balance res = new Balance();
            res.setBTC(balanceBTC.add(btcDecimal).setScale(8, RoundingMode.DOWN).toPlainString());
            res.setETH(balanceETH.add(ethDecimal).setScale(8, RoundingMode.DOWN).toPlainString());
            res.setUSDT(balanceUSDT.add(usdtDecimal).setScale(4, RoundingMode.DOWN).toPlainString());
            res.setUSDC(balanceUSDC.add(usdcDecimal).setScale(4, RoundingMode.DOWN).toPlainString());
            return res;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Balance balance = (Balance) o;
        return Objects.equals(BTC, balance.BTC) && Objects.equals(ETH, balance.ETH) && Objects.equals(USDT, balance.USDT) && Objects.equals(USDC, balance.USDC);
    }

    @Override
    public int hashCode() {
        return Objects.hash(BTC, ETH, USDT, USDC);
    }
}
