package com.fastfood.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fastfood.dto.request.StockReceiptRequest;
import com.fastfood.dto.response.InventoryConsumptionGroupResponse;
import com.fastfood.dto.response.InventoryItemReportResponse;
import com.fastfood.dto.response.StockReceiptResponse;
import com.fastfood.service.IInventoryService;
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

@WebMvcTest(InventoryController.class)
class InventoryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockitoBean
    IInventoryService inventoryService;

    @Test
    void createReceipt() throws Exception {

        StockReceiptRequest request = new StockReceiptRequest();

        when(inventoryService.createStockReceipt(any()))
                .thenReturn(new StockReceiptResponse());

        mockMvc.perform(post("/api/inventory/receipts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(inventoryService).createStockReceipt(any());
    }

    @Test
    void getAllReceipts() throws Exception {

        when(inventoryService.getAllStockReceipts())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/inventory/receipts"))
                .andExpect(status().isOk());

        verify(inventoryService).getAllStockReceipts();
    }

    @Test
    void updateReceipt() throws Exception {

        StockReceiptRequest request = new StockReceiptRequest();

        when(inventoryService.updateStockReceipt(anyString(), any()))
                .thenReturn(new StockReceiptResponse());

        mockMvc.perform(put("/api/inventory/receipts/R001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(inventoryService).updateStockReceipt(eq("R001"), any());
    }

    @Test
    void deleteReceipt() throws Exception {

        mockMvc.perform(delete("/api/inventory/receipts/R001"))
                .andExpect(status().isOk());

        verify(inventoryService).deleteStockReceipt("R001");
    }

    @Test
    void searchReceipts() throws Exception {

        when(inventoryService.searchStockReceipts(any(), any(), any(), any()))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/inventory/receipts/search"))
                .andExpect(status().isOk());

        verify(inventoryService)
                .searchStockReceipts(any(), any(), any(), any());
    }

    @Test
    void inventoryReport() throws Exception {

        when(inventoryService.getInventoryReport())
                .thenReturn(List.of(new InventoryItemReportResponse()));

        mockMvc.perform(get("/api/inventory/report"))
                .andExpect(status().isOk());

        verify(inventoryService).getInventoryReport();
    }

    @Test
    void lowStock() throws Exception {

        when(inventoryService.getLowStockItems())
                .thenReturn(List.of(new InventoryItemReportResponse()));

        mockMvc.perform(get("/api/inventory/low-stock"))
                .andExpect(status().isOk());

        verify(inventoryService).getLowStockItems();
    }

    @Test
    void consumptionHistory() throws Exception {

        when(inventoryService.getConsumptionHistory(any(), any()))
                .thenReturn(List.of(new InventoryConsumptionGroupResponse()));

        mockMvc.perform(get("/api/inventory/consumption-history"))
                .andExpect(status().isOk());

        verify(inventoryService).getConsumptionHistory(any(), any());
    }
}