package com.fastfood.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fastfood.dto.request.OrderRequest;
import com.fastfood.dto.request.PaymentRequest;
import com.fastfood.dto.response.CashierOrderDetailResponse;
import com.fastfood.dto.response.CashierPaymentResponse;
import com.fastfood.dto.response.CashierTableStatusResponse;
import com.fastfood.service.ISalesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SalesController.class)
class SalesControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockitoBean
    ISalesService salesService;

    @Test
    void getTablesStatus() throws Exception {

        when(salesService.getOccupiedTableNumbers())
                .thenReturn(Set.of("N01"));

        mockMvc.perform(get("/api/sales/tables/status"))
                .andExpect(status().isOk());

        verify(salesService).getOccupiedTableNumbers();
    }

    @Test
    void getTableStatusList() throws Exception {

        when(salesService.getTableStatuses())
                .thenReturn(List.of(new CashierTableStatusResponse()));

        mockMvc.perform(get("/api/sales/tables"))
                .andExpect(status().isOk());

        verify(salesService).getTableStatuses();
    }

    @Test
    void getPendingOrder() throws Exception {

        when(salesService.getPendingOrderByTable("N01"))
                .thenReturn(new CashierOrderDetailResponse());

        mockMvc.perform(get("/api/sales/tables/N01/order"))
                .andExpect(status().isOk());

        verify(salesService).getPendingOrderByTable("N01");
    }

    @Test
    void createOrder() throws Exception {

        OrderRequest request = new OrderRequest();

        mockMvc.perform(post("/api/sales/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(salesService).placeOrder(any());
    }

    @Test
    void checkout() throws Exception {

        PaymentRequest request = new PaymentRequest();

        when(salesService.processPayment(any()))
                .thenReturn(new CashierPaymentResponse());

        mockMvc.perform(post("/api/sales/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(salesService).processPayment(any());
    }

    @Test
    void completeOrder() throws Exception {

        mockMvc.perform(post("/api/sales/orders/ORD001/complete"))
                .andExpect(status().isOk());

        verify(salesService).completeOrder("ORD001");
    }
}