package com.bybit;

import com.bybit.merkle.MerkleTree;
import com.bybit.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

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
        boolean validate = validateVersion2(pathJson);
        if(!validate) {
            MerkleTree merkleTree = null;
            try {
                merkleTree = JsonUtil.parseObject(pathJson, MerkleTree.class);
            } catch (Exception e) {
                throw new RuntimeException("json content is error");
            }
            validate = merkleTree.validate();
        }
        System.out.println("validate result is : " + validate);
    }

    static ObjectMapper objectMapper = new ObjectMapper();

    private static boolean validateVersion2(String json) {
        boolean success = false;
        try {
            MerkleTreeVersion2 merkleTree = objectMapper.readValue(json, MerkleTreeVersion2.class);
            success = Optional.ofNullable(merkleTree).map(MerkleTreeVersion2::validate).orElse(false);
        } catch (JsonProcessingException e) {
            System.out.println("fallback to version 1");
        }
        return success;
    }
}
