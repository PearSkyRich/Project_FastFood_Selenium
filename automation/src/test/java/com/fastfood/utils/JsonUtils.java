package com.fastfood.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;

public final class JsonUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonUtils() {
    }

    public static <T> T[] readData(String fileName, Class<T[]> clazz) {

        try (InputStream input = JsonUtils.class
                .getClassLoader()
                .getResourceAsStream(fileName)) {

            if (input == null) {
                throw new RuntimeException("Cannot find file: " + fileName);
            }

            return MAPPER.readValue(input, clazz);

        } catch (Exception e) {
            throw new RuntimeException("Cannot read json file: " + fileName, e);
        }
    }
}