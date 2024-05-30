package com.bybit;

import com.bybit.merkle.MerkleTree;
import com.bybit.merkle.generic.Balance20;
import com.bybit.merkle.generic.Balance32;
import com.bybit.merkle.generic.Balance40;
import com.bybit.merkle.generic.BalanceMnt20;
import com.bybit.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

public class MerkleProofValidator {
    public static void main(String[] args) {
        if (args.length < 1) {
            throw new RuntimeException("param error");
        }
        String jsonfile = args[0];
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(Paths.get(jsonfile));
        } catch (IOException e) {
            throw new RuntimeException("can not found json file: " + jsonfile);
        }

        String pathJson = new String(bytes);
        boolean validate = validateAsset40(pathJson)
                || validateAsset32(pathJson)
                || validateMnt20(pathJson)
                || validate20(pathJson)
                || validate(pathJson);
        System.out.println("validate result is : " + validate);
    }

    static ObjectMapper objectMapper = new ObjectMapper();

    public static boolean validateAsset40(String json) {
        boolean success = false;
        try {
            GenericMerkleTree<Balance40> merkleTree = objectMapper.readValue(json, new TypeReference<GenericMerkleTree<Balance40>>() {
            });
            success = Optional.ofNullable(merkleTree).map(GenericMerkleTree::validate).orElse(false);
        } catch (JsonProcessingException e) {
            System.out.println("fallback to version Asset32");
        }
        return success;
    }

    public static boolean validateAsset32(String json) {
        boolean success = false;
        try {
            GenericMerkleTree<Balance32> merkleTree = objectMapper.readValue(json, new TypeReference<GenericMerkleTree<Balance32>>() {
            });
            success = Optional.ofNullable(merkleTree).map(GenericMerkleTree::validate).orElse(false);
        } catch (JsonProcessingException e) {
            System.out.println("fallback to version AssetMnt20");
        }
        return success;
    }

    public static boolean validateMnt20(String json) {
        boolean success = false;
        try {
            GenericMerkleTree<BalanceMnt20> merkleTree = objectMapper.readValue(json, new TypeReference<GenericMerkleTree<BalanceMnt20>>() {
            });
            success = Optional.ofNullable(merkleTree).map(GenericMerkleTree::validate).orElse(false);
        } catch (IOException e) {
            System.out.println("fallback to version Asset20");
        }
        return success;
    }

    public static boolean validate20(String json) {
        boolean success = false;
        try {
            GenericMerkleTree<Balance20> merkleTree = objectMapper.readValue(json, new TypeReference<GenericMerkleTree<Balance20>>() {
            });
            success = Optional.ofNullable(merkleTree).map(GenericMerkleTree::validate).orElse(false);
        } catch (IOException e) {
            System.out.println("fallback to origin version");
        }
        return success;
    }

    public static boolean validate(String json) {
        try {
            MerkleTree merkleTree = JsonUtil.parseObject(json, MerkleTree.class);
            return merkleTree.validate();
        } catch (Exception e) {
            throw new RuntimeException("json content is error");
        }
    }
}
