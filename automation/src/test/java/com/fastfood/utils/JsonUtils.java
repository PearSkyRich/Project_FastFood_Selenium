package com.fastfood.utils;

import com.fastfood.model.LoginData;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;

public class JsonUtils {

    public static LoginData[] readLoginData() throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        InputStream input = JsonUtils.class
                .getClassLoader()
                .getResourceAsStream("login.json");

        return mapper.readValue(input, LoginData[].class);
    }
}