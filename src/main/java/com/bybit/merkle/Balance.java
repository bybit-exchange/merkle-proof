package com.bybit.merkle;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class Balance {
    @JsonProperty("BTC")
    private String btc;
    @JsonProperty("ETH")
    private String eth;
    @JsonProperty("USDT")
    private String usdt;
    @JsonProperty("USDC")
    private String usdc;

    public boolean validate() {
        try {
            new BigDecimal(this.btc);
            new BigDecimal(this.eth);
            new BigDecimal(this.usdt);
            new BigDecimal(this.usdc);
        } catch (NullPointerException | NumberFormatException e) {
            return false;
        }
        return true;
    }

    public Balance add(Balance balance) {
        try {
            return Balance.builder()
                    .btc(new BigDecimal(balance.btc).add(new BigDecimal(this.btc)).setScale(8, RoundingMode.DOWN).toPlainString())
                    .eth(new BigDecimal(balance.eth).add(new BigDecimal(this.eth)).setScale(8, RoundingMode.DOWN).toPlainString())
                    .usdt(new BigDecimal(balance.usdt).add(new BigDecimal(this.usdt)).setScale(4, RoundingMode.DOWN).toPlainString())
                    .usdc(new BigDecimal(balance.usdc).add(new BigDecimal(this.usdc)).setScale(4, RoundingMode.DOWN).toPlainString())
                    .build();
        } catch (NullPointerException | NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }
}
