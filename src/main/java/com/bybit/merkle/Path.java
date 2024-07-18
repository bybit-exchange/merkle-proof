package com.bybit.merkle;

import com.bybit.util.CryptoUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class Path {
    private String auditId;
    private Integer height;
    private Integer type;
    private String hash;
    private Balance balance;
    private String nonce;

    public Path(int height, Integer type, String hash, Balance balance) {
        this.height = height;
        this.type = type;
        this.hash = hash;
        this.balance = balance;
    }

    public boolean validate() {
        if (this.balance == null || !this.balance.validate() || hash == null || hash.isEmpty() || this.type < 1 || this.type > 4) {
            return false;
        }
        return this.height >= 1;
    }

    public static Path instance(String leftHash, String rightHash, Balance balance1, Balance balance2, int height, Integer type) {
        if (!balance1.validate() || !balance2.validate()) {
            return new Path();
        }
        Balance balance = balance1.add(balance2);
        String data = "" + leftHash + rightHash + balance.getBtc() + balance.getEth() + balance.getUsdt() + balance.getUsdc() + height;
        String s = CryptoUtil.sha256Str(data);
        return new Path(height, type, s, balance);
    }
}
