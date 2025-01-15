package com.bybit;

import com.bybit.merkle.MerkleTree;
import com.bybit.merkle.generic.Balance20;
import com.bybit.merkle.generic.Balance32;
import com.bybit.merkle.generic.Balance40;
import com.bybit.merkle.generic.Balance40V2;
import com.bybit.merkle.generic.Balance40V3;
import com.bybit.merkle.generic.Balance40V4;
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
    private final static ObjectMapper objectMapper = new ObjectMapper();

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

        System.out.println("Validate result is : " + validation(pathJson));
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

    public static boolean validation(String json) {
        return validateAsset40V4(json)
                || validateAsset40V3(json)
                || validateAsset40V2(json)
                || validateAsset40(json)
                || validateAsset32(json)
                || validateMnt20(json)
                || validate20(json)
                || validate(json);
    }

    public static boolean validateAsset40V4(String json) {
        boolean success = false;
        try {
            GenericMerkleTree<Balance40V4> merkleTree = objectMapper.readValue(json, new TypeReference<GenericMerkleTree<Balance40V4>>() {
            });
            success = Optional.ofNullable(merkleTree).map(GenericMerkleTree::validate).orElse(false);
        } catch (IOException e) {
            System.out.println("Fallback to version Asset40V3");
        }
        return success;
    }

    public static boolean validateAsset40V3(String json) {
        boolean success = false;
        try {
            GenericMerkleTree<Balance40V3> merkleTree = objectMapper.readValue(json, new TypeReference<GenericMerkleTree<Balance40V3>>() {
            });
            success = Optional.ofNullable(merkleTree).map(GenericMerkleTree::validate).orElse(false);
        } catch (IOException e) {
            System.out.println("Fallback to version Asset40V2");
        }
        return success;
    }

    public static boolean validateAsset40V2(String json) {
        boolean success = false;
        try {
            GenericMerkleTree<Balance40V2> merkleTree = objectMapper.readValue(json, new TypeReference<GenericMerkleTree<Balance40V2>>() {
            });
            success = Optional.ofNullable(merkleTree).map(GenericMerkleTree::validate).orElse(false);
        } catch (IOException e) {
            System.out.println("Fallback to version Asset40");
        }
        return success;
    }

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
        boolean success = false;
        try {
            MerkleTree merkleTree = objectMapper.readValue(json, MerkleTree.class);
            success = merkleTree.validate();
        } catch (IOException e) {
            System.out.println("Json content is error");
        }
        return success;
    }
}
