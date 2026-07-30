package com.fastfood.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fastfood.dto.request.IngredientRequest;
import com.fastfood.dto.response.IngredientResponse;
import com.fastfood.service.IIngredientService;
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

@WebMvcTest(IngredientController.class)
class IngredientControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockitoBean
    IIngredientService service;

    @Test
    void getAllIngredient() throws Exception{

        when(service.getAllIngredient()).thenReturn(List.of());

        mockMvc.perform(get("/api/ingredient"))
                .andExpect(status().isOk());

        verify(service).getAllIngredient();
    }

    @Test
    void getIngredientById() throws Exception{

        when(service.getIngredientById("NL01"))
                .thenReturn(new IngredientResponse());

        mockMvc.perform(get("/api/ingredient/NL01"))
                .andExpect(status().isOk());

        verify(service).getIngredientById("NL01");
    }

    @Test
    void createIngredient() throws Exception{

        IngredientRequest request=new IngredientRequest();

        when(service.saveIngredient(any()))
                .thenReturn(new IngredientResponse());

        mockMvc.perform(post("/api/ingredient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(service).saveIngredient(any());
    }

    @Test
    void updateIngredient() throws Exception{

        IngredientRequest request=new IngredientRequest();

        when(service.updateIngredient(any(),anyString()))
                .thenReturn(new IngredientResponse());

        mockMvc.perform(put("/api/ingredient/NL01")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(service).updateIngredient(any(),eq("NL01"));
    }

    @Test
    void deleteIngredient() throws Exception{

        mockMvc.perform(delete("/api/ingredient/NL01"))
                .andExpect(status().isOk());

        verify(service).deleteIngredient("NL01");
    }
}