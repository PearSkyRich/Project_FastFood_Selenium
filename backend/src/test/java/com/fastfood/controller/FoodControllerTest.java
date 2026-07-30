package com.fastfood.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fastfood.dto.request.FoodRequest;
import com.fastfood.dto.response.FoodCostResponse;
import com.fastfood.dto.response.FoodKitchenResponse;
import com.fastfood.dto.response.FoodMenuResponse;
import com.fastfood.service.IFoodService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FoodController.class)
class FoodControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockitoBean
    IFoodService foodService;

    @Test
    void getAllMenu() throws Exception {

        when(foodService.getAllMenu())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/foods/menu"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(foodService).getAllMenu();
    }

    @Test
    void getMenuById() throws Exception {

        when(foodService.getMenuById("H001"))
                .thenReturn(new FoodMenuResponse());

        mockMvc.perform(get("/api/foods/menu/H001"))
                .andExpect(status().isOk());

        verify(foodService).getMenuById("H001");
    }

    @Test
    void getKitchenFood() throws Exception {

        when(foodService.getFoodForKitchen("H001"))
                .thenReturn(new FoodKitchenResponse());

        mockMvc.perform(get("/api/foods/kitchen/H001"))
                .andExpect(status().isOk());

        verify(foodService).getFoodForKitchen("H001");
    }

    @Test
    void getFoodCosts() throws Exception {

        when(foodService.getFoodCosts())
                .thenReturn(List.of(new FoodCostResponse()));

        mockMvc.perform(get("/api/foods/costs"))
                .andExpect(status().isOk());

        verify(foodService).getFoodCosts();
    }

    @Test
    void createFood() throws Exception {

        FoodRequest request = new FoodRequest();

        when(foodService.createFood(any()))
                .thenReturn(new FoodKitchenResponse());

        mockMvc.perform(post("/api/foods")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(foodService).createFood(any());
    }

    @Test
    void updateFood() throws Exception {

        FoodRequest request = new FoodRequest();

        when(foodService.updateFood(anyString(), any()))
                .thenReturn(new FoodKitchenResponse());

        mockMvc.perform(put("/api/foods/H001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(foodService).updateFood(eq("H001"), any());
    }

    @Test
    void deleteFood() throws Exception {

        mockMvc.perform(delete("/api/foods/H001"))
                .andExpect(status().isOk());

        verify(foodService).deleteFood("H001");
    }
}