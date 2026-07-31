package com.fastfood.integration.controller;

import org.junit.jupiter.api.Test;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class AdminDashboardControllerIntegrationTest extends BaseIntegrationTest {

    @Test
    void dashboard_success() throws Exception {

        mockMvc.perform(
                        get("/api/admin/dashboard")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

    }

    @Test
    void dashboard_with_filter() throws Exception {

        mockMvc.perform(
                        get("/api/admin/dashboard")
                                .param("fromDate","2026-01-01")
                                .param("toDate","2026-07-31")
                                .param("topN","5")
                )
                .andExpect(status().isOk());

    }

}