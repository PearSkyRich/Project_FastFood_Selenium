package com.fastfood.service.impl;

import com.fastfood.dto.response.AdminDashboardResponse;
import com.fastfood.dto.response.TopOrderedFoodResponse;
import com.fastfood.repository.OrderDetailRepository;
import com.fastfood.repository.OrderRepository;
import com.fastfood.repository.SalesInvoiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminDashboardServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderDetailRepository orderDetailRepository;

    @Mock
    private SalesInvoiceRepository salesInvoiceRepository;

    @InjectMocks
    private AdminDashboardServiceImpl service;

    private LocalDate from;
    private LocalDate to;

    @BeforeEach
    void setup() {
        from = LocalDate.of(2026,1,1);
        to = LocalDate.of(2026,1,31);
    }

    @Test
    void getDashboard_Success() {

        when(salesInvoiceRepository.sumRevenueInRange(any(), any()))
                .thenReturn(BigDecimal.valueOf(1000));

        when(salesInvoiceRepository.countInvoicesInRange(any(), any()))
                .thenReturn(2L);

        when(orderRepository.countOrdersInRange(any(), any()))
                .thenReturn(5L);

        when(orderDetailRepository.sumProductionCostOfPaidOrdersInRange(any(), any()))
                .thenReturn(BigDecimal.valueOf(400));

        Object[] row = new Object[]{
                "F01",
                "Burger",
                "img.jpg",
                10L,
                BigDecimal.valueOf(600)
        };

        List<Object[]> rows = new ArrayList<>();
        rows.add(row);

        when(orderDetailRepository.findTopOrderedFoodsInRange(
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                any(Pageable.class)
        )).thenReturn(rows);

        AdminDashboardResponse response =
                service.getDashboard(from,to,5);

        assertNotNull(response);

        assertEquals(5,response.getOrderCount());

        assertEquals(
                BigDecimal.valueOf(600),
                response.getProfit()
        );

        assertEquals(
                BigDecimal.valueOf(500.00).setScale(2),
                response.getAverageRevenue()
        );

        assertEquals(
                BigDecimal.valueOf(200.00).setScale(2),
                response.getAverageImportCost()
        );

        assertEquals(1,response.getTopOrderedFoods().size());

        TopOrderedFoodResponse food =
                response.getTopOrderedFoods().get(0);

        assertEquals("F01",food.getIdFood());
        assertEquals("Burger",food.getFoodName());
        assertEquals(10,food.getQuantityOrdered());
    }

    @Test
    void getDashboard_InvalidDate() {

        LocalDate from = LocalDate.of(2026,2,1);
        LocalDate to = LocalDate.of(2026,1,1);

        assertThrows(
                IllegalArgumentException.class,
                ()->service.getDashboard(from,to,5)
        );
    }

    @Test
    void getDashboard_DefaultTopN() {

        when(salesInvoiceRepository.sumRevenueInRange(any(), any()))
                .thenReturn(BigDecimal.ZERO);

        when(salesInvoiceRepository.countInvoicesInRange(any(), any()))
                .thenReturn(0L);

        when(orderRepository.countOrdersInRange(any(), any()))
                .thenReturn(0L);

        when(orderDetailRepository.sumProductionCostOfPaidOrdersInRange(any(), any()))
                .thenReturn(BigDecimal.ZERO);

        when(orderDetailRepository.findTopOrderedFoodsInRange(
                any(),
                any(),
                any(Pageable.class)))
                .thenReturn(List.of());

        AdminDashboardResponse response =
                service.getDashboard(from,to,0);

        assertNotNull(response);

        verify(orderDetailRepository)
                .findTopOrderedFoodsInRange(
                        any(),
                        any(),
                        ArgumentMatchers.argThat(pageable ->
                                pageable.getPageSize()==6)
                );
    }

    @Test
    void getDashboard_NoInvoice() {

        when(salesInvoiceRepository.sumRevenueInRange(any(), any()))
                .thenReturn(BigDecimal.valueOf(1000));

        when(salesInvoiceRepository.countInvoicesInRange(any(), any()))
                .thenReturn(0L);

        when(orderRepository.countOrdersInRange(any(), any()))
                .thenReturn(0L);

        when(orderDetailRepository.sumProductionCostOfPaidOrdersInRange(any(), any()))
                .thenReturn(BigDecimal.valueOf(500));

        when(orderDetailRepository.findTopOrderedFoodsInRange(any(), any(), any()))
                .thenReturn(List.of());

        AdminDashboardResponse response =
                service.getDashboard(from,to,5);

        assertEquals(BigDecimal.ZERO,response.getAverageRevenue());

        assertEquals(BigDecimal.ZERO,response.getAverageImportCost());
    }

    @Test
    void getDashboard_NullRevenue() {

        when(salesInvoiceRepository.sumRevenueInRange(any(), any()))
                .thenReturn(null);

        when(salesInvoiceRepository.countInvoicesInRange(any(), any()))
                .thenReturn(0L);

        when(orderRepository.countOrdersInRange(any(), any()))
                .thenReturn(0L);

        when(orderDetailRepository.sumProductionCostOfPaidOrdersInRange(any(), any()))
                .thenReturn(null);

        when(orderDetailRepository.findTopOrderedFoodsInRange(any(), any(), any()))
                .thenReturn(List.of());

        AdminDashboardResponse response =
                service.getDashboard(from,to,5);

        assertEquals(BigDecimal.ZERO,response.getProfit());

        assertEquals(BigDecimal.ZERO,response.getAverageRevenue());
    }

    @Test
    void getDashboard_DefaultDate() {

        when(salesInvoiceRepository.sumRevenueInRange(any(), any()))
                .thenReturn(BigDecimal.ZERO);

        when(salesInvoiceRepository.countInvoicesInRange(any(), any()))
                .thenReturn(0L);

        when(orderRepository.countOrdersInRange(any(), any()))
                .thenReturn(0L);

        when(orderDetailRepository.sumProductionCostOfPaidOrdersInRange(any(), any()))
                .thenReturn(BigDecimal.ZERO);

        when(orderDetailRepository.findTopOrderedFoodsInRange(any(), any(), any()))
                .thenReturn(List.of());

        AdminDashboardResponse response =
                service.getDashboard(null,null,5);

        assertNotNull(response);

        assertEquals(LocalDate.now(),response.getFromDate());

        assertEquals(LocalDate.now(),response.getToDate());
    }

}