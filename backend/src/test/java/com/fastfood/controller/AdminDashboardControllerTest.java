package com.fastfood.controller;

import com.fastfood.dto.response.AdminDashboardResponse;
import com.fastfood.service.IAdminDashboardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminDashboardController.class)
class AdminDashboardControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    IAdminDashboardService service;

    @Test
    void getDashboard() throws Exception {

        when(service.getDashboard(any(), any(), anyInt()))
                .thenReturn(new AdminDashboardResponse());

        mockMvc.perform(get("/api/admin/dashboard")
                        .param("fromDate","2026-01-01")
                        .param("toDate","2026-12-31")
                        .param("topN","5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(service).getDashboard(any(LocalDate.class),any(LocalDate.class),eq(5));
    }
}