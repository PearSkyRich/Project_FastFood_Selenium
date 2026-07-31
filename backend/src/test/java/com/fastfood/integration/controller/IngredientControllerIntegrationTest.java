package com.fastfood.integration.controller;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class IngredientControllerIntegrationTest extends BaseIntegrationTest {

    @Test
    void get_all_ingredient() throws Exception {

        mockMvc.perform(
                        get("/api/ingredient")
                )
                .andExpect(status().isOk());

    }

    @Test
    void get_ingredient_by_id() throws Exception {

        mockMvc.perform(
                        get("/api/ingredient/NL001")
                )
                .andExpect(status().isOk());

    }

}