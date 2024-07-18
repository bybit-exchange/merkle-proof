package com.bybit;

import com.bybit.merkle.MerkleTree;
import com.bybit.merkle.generic.Balance20;
import com.bybit.merkle.generic.Balance32;
import com.bybit.merkle.generic.Balance40;
import com.bybit.merkle.generic.BalanceMnt20;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class MerkleProofValidator {
    public static void main(String[] args) {
        if (args.length < 1) {
            throw new RuntimeException("Param error");
        }
        String jsonfile = args[0];
        byte[] bytes;
        try {
            Path filePath = validatePath(jsonfile);
            bytes = Files.readAllBytes(filePath);
        } catch (IOException | IllegalArgumentException e) {
            throw new RuntimeException("Cannot found or access json file: " + jsonfile);
        }

        String pathJson;
        try {
            pathJson = new String(bytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Unsupported encoding for file: " + jsonfile);
        }

        boolean validate = validateAsset40(pathJson)
                || validateAsset32(pathJson)
                || validateMnt20(pathJson)
                || validate20(pathJson)
                || validate(pathJson);
        System.out.println("Validate result is : " + validate);
    }

    private static Path validatePath(String path) {
        Path filePath = Paths.get(path).normalize();
        Path validPath = Paths.get(".").toAbsolutePath().normalize();
        System.out.println("Currently execute path is: " + validPath);
        if (!filePath.startsWith(validPath)) {
            throw new InvalidPathException(path, "Invalid file path");
        }
        return filePath;
    }

    static ObjectMapper objectMapper = new ObjectMapper();

    public static boolean validateAsset40(String json) {
        boolean success = false;
        try {
            GenericMerkleTree<Balance40> merkleTree = objectMapper.readValue(json, new TypeReference<GenericMerkleTree<Balance40>>() {
            });
            success = Optional.ofNullable(merkleTree).map(GenericMerkleTree::validate).orElse(false);
        } catch (IOException e) {
            System.out.println("Fallback to version Asset32");
        }
        return success;
    }

    public static boolean validateAsset32(String json) {
        boolean success = false;
        try {
            GenericMerkleTree<Balance32> merkleTree = objectMapper.readValue(json, new TypeReference<GenericMerkleTree<Balance32>>() {
            });
            success = Optional.ofNullable(merkleTree).map(GenericMerkleTree::validate).orElse(false);
        } catch (IOException e) {
            System.out.println("Fallback to version AssetMnt20");
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
            System.out.println("Fallback to version Asset20");
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
            System.out.println("Fallback to origin version");
        }
        return success;
    }

    public static boolean validate(String json) {
        try {
            MerkleTree merkleTree = objectMapper.readValue(json, MerkleTree.class);
            return merkleTree.validate();
        } catch (IOException e) {
            throw new RuntimeException("Json content is error", e);
        }
    }
}
