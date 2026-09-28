package com.bybit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MerkleProofValidatorTest {

    @Test
    public void testValidatePathAcceptsRelativePaths() throws Exception {
        Method method = MerkleProofValidator.class.getDeclaredMethod("validatePath", String.class);
        method.setAccessible(true);

        Path path =
                (Path) method.invoke(null, "src/test/resources/mock_user_merkle_tree_path_40.json");

        Assertions.assertTrue(Files.exists(path));
        Assertions.assertTrue(path.isAbsolute());
    }

    @Test
    public void testMerkleProofValidation() {
        List<String> files = new ArrayList<>();
        files.add("mock_user_merkle_tree_path_40.json");
        files.add("mock_user_merkle_tree_path_40_v2.json");
        files.add("mock_user_merkle_tree_path_40_v3.json");
        files.add("mock_user_merkle_tree_path_40_v4.json");
        files.add("mock_user_merkle_tree_path_40_v5.json");
        files.forEach(
                file -> {
                    System.out.println("Validate file: " + file);
                    String json = read(file);
                    Assertions.assertTrue(MerkleProofValidator.validation(json));
                });
    }

    private String read(String fileName) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
                BufferedReader reader =
                        new BufferedReader(
                                new InputStreamReader(Objects.requireNonNull(inputStream)))) {
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
