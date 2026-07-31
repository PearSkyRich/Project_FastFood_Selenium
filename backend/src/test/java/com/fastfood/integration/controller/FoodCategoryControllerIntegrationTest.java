package com.fastfood.integration.controller;

import com.fastfood.integration.controller.BaseIntegrationTest;
import org.junit.jupiter.api.Test;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class FoodCategoryControllerIntegrationTest extends BaseIntegrationTest {


    @Test
    void get_all_category() throws Exception {


        mockMvc.perform(get("/api/foodCategory"))
                .andExpect(status().isOk());

    }

    @Test
    void get_category_by_id() throws Exception {

        mockMvc.perform(
                        get("/api/foodCategory/LH001")
                )
                .andExpect(status().isOk());

    }

}