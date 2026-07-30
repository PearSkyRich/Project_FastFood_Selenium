package com.fastfood.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fastfood.dto.request.FoodCategoryRequest;
import com.fastfood.dto.response.FoodCategoryResponse;
import com.fastfood.service.IFoodCategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FoodCategoryController.class)
class FoodCategoryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockitoBean
    IFoodCategoryService service;

    @Test
    void getAllCategory() throws Exception {

        when(service.getAllCategory()).thenReturn(List.of());

        mockMvc.perform(get("/api/foodCategory"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(service).getAllCategory();
    }

    @Test
    void getCategoryById() throws Exception {

        when(service.getCategoryById("CAT01"))
                .thenReturn(new FoodCategoryResponse());

        mockMvc.perform(get("/api/foodCategory/CAT01"))
                .andExpect(status().isOk());

        verify(service).getCategoryById("CAT01");
    }

    @Test
    void createCategory() throws Exception {

        FoodCategoryRequest request=new FoodCategoryRequest();

        when(service.saveCategory(any()))
                .thenReturn(new FoodCategoryResponse());

        mockMvc.perform(post("/api/foodCategory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(service).saveCategory(any());
    }

    @Test
    void updateCategory() throws Exception {

        FoodCategoryRequest request=new FoodCategoryRequest();

        when(service.updateCategory(anyString(),any()))
                .thenReturn(new FoodCategoryResponse());

        mockMvc.perform(put("/api/foodCategory/CAT01")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(service).updateCategory(eq("CAT01"),any());
    }

    @Test
    void deleteCategory() throws Exception {

        mockMvc.perform(delete("/api/foodCategory/CAT01"))
                .andExpect(status().isOk());

        verify(service).deleteCategory("CAT01");
    }
}