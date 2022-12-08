package com.bybit.merkle;

import com.bybit.util.CryptoUtil;

public class Path {
    private String auditId;
    private Integer height;
    private Integer type;
    private String hash;
    private Balance balance;
    private String nonce;

    public String getAuditId() {
        return auditId;
    }

    public void setAuditId(String auditId) {
        this.auditId = auditId;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Balance getBalance() {
        return balance;
    }

    public void setBalance(Balance balance) {
        this.balance = balance;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public Path(){}

    public Path(int height, Integer type, String hash, Balance balance){
        this.setHeight(height);
        this.setType(type);
        this.setHash(hash);
        this.setBalance(balance);
    }

    public boolean validate(){
        if(this.balance == null || !this.balance.validate() || hash == null || hash.length() == 0 || this.type < 1 || this.type > 4) {
            return false;
        }
        return this.height >= 1;
    }

    public static Path instance(String leftHash, String rightHash, Balance balance1, Balance balance2, int height, Integer type){
        if(!balance1.validate() || !balance2.validate()) {
            return new Path();
        }
        Balance balance = balance1.add(balance2);
        String data = "" + leftHash + rightHash + balance.getBTC() + balance.getETH() + balance.getUSDT() + balance.getUSDC() + height;
        String s = CryptoUtil.sha256Str(data);
        return new Path(height, type, s, balance);
    }
}
