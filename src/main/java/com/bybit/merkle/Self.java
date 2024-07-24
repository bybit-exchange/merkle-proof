package com.bybit.merkle;

import com.bybit.util.CryptoUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Self {
    private String auditId;
    private String userHash;
    private String nonce;
    private String hash;
    private Balance balance;
    private Integer type;
    private Integer height;

    public boolean validate() {
        String data = userHash + balance.getBtc() + balance.getEth() + balance.getUsdt() + balance.getUsdc();
        String s = CryptoUtil.sha256Str(data);
        return s.equals(hash);
    }
}
