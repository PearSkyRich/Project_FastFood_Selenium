package com.fastfood.integration.controller;

import org.junit.jupiter.api.Test;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class SalesControllerIntegrationTest extends BaseIntegrationTest {

    @Test
    void get_table_status() throws Exception {

        mockMvc.perform(
                        get("/api/sales/tables")
                )
                .andExpect(status().isOk());

    }

    @Test
    void get_occupied_tables() throws Exception {

        mockMvc.perform(
                        get("/api/sales/tables/status")
                )
                .andExpect(status().isOk());

    }


}