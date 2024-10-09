package com.bybit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class MerkleProofValidatorTest {

    @Test
    public void testMerkleProofForToken40() {
        String fileName = "mock_user_merkle_tree_path_40.json";
        String json = read(fileName);
        Assertions.assertTrue(MerkleProofValidator.validateAsset40(json));
    }

    @Test
    public void testMerkleProofForToken40V2() {
        String fileName = "mock_user_merkle_tree_path_40_v2.json";
        String json = read(fileName);
        Assertions.assertTrue(MerkleProofValidator.validateAsset40V2(json));
    }

    @Test
    public void testMerkleProofForToken40V3() {
        String fileName = "mock_user_merkle_tree_path_40_v3.json";
        String json = read(fileName);
        Assertions.assertTrue(MerkleProofValidator.validateAsset40V3(json));
    }

    private String read(String fileName) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
