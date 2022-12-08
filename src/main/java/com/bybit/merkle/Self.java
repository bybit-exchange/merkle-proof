package com.bybit.merkle;

import com.bybit.util.CryptoUtil;

public class Self {
    private String auditId;
    private String userHash;
    private String nonce;
    private String hash;
    private Balance balance;
    private Integer type;
    private Integer height;

    public String getAuditId() {
        return auditId;
    }

    public void setAuditId(String auditId) {
        this.auditId = auditId;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getUserHash() {
        return userHash;
    }

    public void setUserHash(String userHash) {
        this.userHash = userHash;
    }

    public Balance getBalance() {
        return balance;
    }

    public void setBalance(Balance balance) {
        this.balance = balance;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public boolean validate(){
        String data = userHash + balance.getBTC() + balance.getETH() + balance.getUSDT() + balance.getUSDC();
        String s= CryptoUtil.sha256Str(data);
        return s.equals(hash);
    }
}
