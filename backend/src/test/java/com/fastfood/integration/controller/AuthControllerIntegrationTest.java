package com.fastfood.integration.controller;


import com.fastfood.integration.controller.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class AuthControllerIntegrationTest extends BaseIntegrationTest {


    @Test
    void login_success() throws Exception {

        String json = """
                {
                    "username":"admin",
                    "password":"a123456"
                }
                """;
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }


    @Test
    void login_wrong_password() throws Exception {

        String json = """
                {
                    "username":"admin",
                    "password":"wrong"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void logout_success() throws Exception {

        mockMvc.perform(post("/api/auth/logout"))
                .andExpect(status().isOk());

    }

}