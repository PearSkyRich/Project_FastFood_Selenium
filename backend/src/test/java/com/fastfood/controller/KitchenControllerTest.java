package com.fastfood.controller;

import com.fastfood.dto.response.KitchenFoodPendingResponse;
import com.fastfood.dto.response.KitchenTableOrderResponse;
import com.fastfood.service.IKitchenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(KitchenController.class)
class KitchenControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    IKitchenService kitchenService;

    @Test
    void getPendingOrders() throws Exception {

        when(kitchenService.getPendingOrdersByTable())
                .thenReturn(List.of(new KitchenTableOrderResponse()));

        mockMvc.perform(get("/api/kitchen/orders"))
                .andExpect(status().isOk());

        verify(kitchenService).getPendingOrdersByTable();
    }

    @Test
    void getCompletedOrders() throws Exception {

        when(kitchenService.getCompletedOrders())
                .thenReturn(List.of(new KitchenTableOrderResponse()));

        mockMvc.perform(get("/api/kitchen/orders/completed"))
                .andExpect(status().isOk());

        verify(kitchenService).getCompletedOrders();
    }

    @Test
    void getRemainingFoods() throws Exception {

        when(kitchenService.getRemainingFoodSummary())
                .thenReturn(List.of(new KitchenFoodPendingResponse()));

        mockMvc.perform(get("/api/kitchen/foods/remaining"))
                .andExpect(status().isOk());

        verify(kitchenService).getRemainingFoodSummary();
    }

    @Test
    void markServed() throws Exception {

        mockMvc.perform(post("/api/kitchen/orders/items/1/served"))
                .andExpect(status().isOk());

        verify(kitchenService).markOrderItemServed(1L);
    }
}