package com.bybit.merkle.generic;

import com.bybit.util.CryptoUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Path<T extends Balance> {
    private String auditId;
    private Integer height;
    private Integer type;
    private String nonce;
    protected String hash;
    protected T balance;

    public Path(String leftHash, String rightHash, T balance1, T balance2, int height, Integer type) {
        if (balance1.validate() && balance2.validate()) {
            T balance = (T) balance1.add(balance2);
            String balanceConcatenation = balance.concatBalances();
            String data = leftHash + rightHash + balanceConcatenation + height;
            String hash = CryptoUtil.sha256Str(data);

            this.height = height;
            this.type = type;
            this.hash = hash;
            this.balance = balance;
        }
    }

    public boolean validate() {
        if (this.balance == null
                || !this.balance.validate()
                || this.hash == null
                || this.hash.isEmpty()
                || this.type < 1
                || this.type > 4) {
            return false;
        }
        return this.height >= 1;
    }
}
