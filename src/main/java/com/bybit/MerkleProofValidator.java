package com.bybit;

import com.bybit.merkle.MerkleTree;
import com.bybit.util.JsonUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MerkleProofValidator {
    public static void main(String[] args) {
        if(args.length < 1) {
            throw new RuntimeException("param error");
        }
        String jsonfile = args[0];
        byte[] bytes = null;
        try {
            bytes = Files.readAllBytes(Paths.get(jsonfile));
        } catch (IOException e) {
            throw new RuntimeException("can not found json file: " + jsonfile);
        }
        String pathJson = new String(bytes);
        MerkleTree merkleTree = null;
        try {
            merkleTree = JsonUtil.parseObject(pathJson, MerkleTree.class);
        } catch (Exception e) {
            throw new RuntimeException("json content is error");
        }
        boolean validate = merkleTree.validate();
        System.out.println("validate result is : " + validate);
    }
}
