package com.bybit.util;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CryptoUtil {

    public static byte[] sha256(String origin) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(origin.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String sha256Str(String origin){
        byte[] bytes = sha256(origin);
        // Convert byte array into signum representation
        BigInteger no = new BigInteger(1, bytes);
        // Convert message digest into hex value
        String hashtext = no.toString(16);
        while (hashtext.length() < 64) {
            hashtext = "0" + hashtext;
        }
        return hashtext;
    }
}
