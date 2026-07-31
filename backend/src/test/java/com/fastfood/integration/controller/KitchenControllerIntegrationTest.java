package com.fastfood.integration.controller;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class KitchenControllerIntegrationTest extends BaseIntegrationTest {

    @Test
    void get_pending_orders() throws Exception {

        mockMvc.perform(
                        get("/api/kitchen/orders")
                )
                .andExpect(status().isOk());

    }

    @Test
    void get_completed_orders() throws Exception {

        mockMvc.perform(
                        get("/api/kitchen/orders/completed")
                )
                .andExpect(status().isOk());

    }

    @Test
    void get_remaining_food() throws Exception {
        mockMvc.perform(
                        get("/api/kitchen/foods/remaining")
                )
                .andExpect(status().isOk());

    }

}