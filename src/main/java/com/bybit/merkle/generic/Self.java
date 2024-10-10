package com.bybit.merkle.generic;

import com.bybit.util.CryptoUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Self<T extends Balance> extends Path<T> {
    private String userHash;

    public boolean validate() {
        if (this.userHash == null) {
            throw new RuntimeException("User hash should be not null");
        }
        String data = userHash + balance.concatBalances();
        String calculatedHash = CryptoUtil.sha256Str(data);
        return calculatedHash.equals(hash);
    }
}
