package com.fastfood.integration.controller;

import com.fastfood.integration.controller.BaseIntegrationTest;
import org.junit.jupiter.api.Test;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class FoodControllerIntegrationTest extends BaseIntegrationTest {

    @Test
    void get_menu_success() throws Exception {

        mockMvc.perform(get("/api/foods/menu"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }
    @Test
    void get_food_by_id_success() throws Exception {
        mockMvc.perform(
                        get("/api/foods/menu/H001")
                )
                .andExpect(status().isOk());

    }

    @Test
    void get_food_cost_success() throws Exception {
        mockMvc.perform(
                        get("/api/foods/costs")
                )
                .andExpect(status().isOk());
    }
}