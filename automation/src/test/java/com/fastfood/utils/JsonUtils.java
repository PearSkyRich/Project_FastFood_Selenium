package com.fastfood.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;

public class JsonUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T[] readData(String fileName, Class<T[]> clazz) throws Exception {

        InputStream input = JsonUtils.class
                .getClassLoader()
                .getResourceAsStream(fileName);

        if (input == null) {
            throw new RuntimeException("Không tìm thấy file: " + fileName);
        }

        return mapper.readValue(input, clazz);
    }
}