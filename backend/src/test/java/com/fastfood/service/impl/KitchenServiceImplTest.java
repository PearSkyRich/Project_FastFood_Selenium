package com.fastfood.service.impl;

import com.fastfood.dto.response.KitchenTableOrderResponse;
import com.fastfood.entity.catalog.Food;
import com.fastfood.entity.catalog.FoodIngredient;
import com.fastfood.entity.catalog.Ingredient;
import com.fastfood.entity.transaction.Order;
import com.fastfood.entity.transaction.OrderDetail;
import com.fastfood.repository.FoodRepository;
import com.fastfood.repository.OrderDetailRepository;
import com.fastfood.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KitchenServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderDetailRepository orderDetailRepository;

    @Mock
    private FoodRepository foodRepository;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private KitchenServiceImpl service;

    //====================================================
    // Helper
    //====================================================

    private Food createFood() {

        Food food = new Food();
        food.setIdFood("H001");
        food.setFoodName("Burger");
        food.setImageUrlFood("burger.png");

        return food;
    }

    private Order createPaidOrder(String table) {

        Order order = new Order();
        order.setIdOrder("HD001");
        order.setTableNumber(table);
        order.setStatus("PAID");
        order.setOrderTime(LocalDateTime.now().minusMinutes(20));
        order.setOrderDetails(new ArrayList<>());

        return order;
    }

    private OrderDetail createPendingDetail(Order order) {

        OrderDetail detail = new OrderDetail();

        detail.setId(1L);
        detail.setOrder(order);
        detail.setFood(createFood());
        detail.setQuantity(2);
        detail.setStatus("PENDING");

        return detail;
    }

    private OrderDetail createServedDetail(Order order) {

        OrderDetail detail = new OrderDetail();

        detail.setId(2L);
        detail.setOrder(order);
        detail.setFood(createFood());
        detail.setQuantity(1);
        detail.setStatus("SERVED");

        return detail;
    }

    //====================================================
    // getPendingOrdersByTable
    //====================================================

    @Test
    void getPendingOrdersByTable_Success() {

        Order order = createPaidOrder("B01");

        OrderDetail detail = createPendingDetail(order);

        order.getOrderDetails().add(detail);

        when(orderRepository.findOrdersWithDetailsByDate(any(), any()))
                .thenReturn(List.of(order));

        List<KitchenTableOrderResponse> result =
                service.getPendingOrdersByTable();

        assertEquals(1, result.size());

        KitchenTableOrderResponse table = result.get(0);

        assertEquals("B01", table.getTableNumber());

        assertEquals(1, table.getItems().size());

        assertEquals("Burger",
                table.getItems().get(0).getFoodName());

        verify(orderRepository)
                .findOrdersWithDetailsByDate(any(), any());
    }

    @Test
    void getPendingOrdersByTable_NoOrders() {

        when(orderRepository.findOrdersWithDetailsByDate(any(), any()))
                .thenReturn(List.of());

        List<KitchenTableOrderResponse> result =
                service.getPendingOrdersByTable();

        assertTrue(result.isEmpty());
    }

    @Test
    void getPendingOrdersByTable_OrderNotPaid() {

        Order order = createPaidOrder("B01");
        order.setStatus("PENDING");

        order.getOrderDetails().add(createPendingDetail(order));

        when(orderRepository.findOrdersWithDetailsByDate(any(), any()))
                .thenReturn(List.of(order));

        List<KitchenTableOrderResponse> result =
                service.getPendingOrdersByTable();

        assertTrue(result.isEmpty());
    }

    @Test
    void getPendingOrdersByTable_OrderDetailNull() {

        Order order = createPaidOrder("B01");

        order.setOrderDetails(null);

        when(orderRepository.findOrdersWithDetailsByDate(any(), any()))
                .thenReturn(List.of(order));

        List<KitchenTableOrderResponse> result =
                service.getPendingOrdersByTable();

        assertTrue(result.isEmpty());
    }

    @Test
    void getPendingOrdersByTable_FoodNull() {

        Order order = createPaidOrder("B01");

        OrderDetail detail = createPendingDetail(order);
        detail.setFood(null);

        order.getOrderDetails().add(detail);

        when(orderRepository.findOrdersWithDetailsByDate(any(), any()))
                .thenReturn(List.of(order));

        List<KitchenTableOrderResponse> result =
                service.getPendingOrdersByTable();

        assertTrue(result.isEmpty());
    }

    @Test
    void getPendingOrdersByTable_DetailAlreadyServed() {

        Order order = createPaidOrder("B01");

        order.getOrderDetails().add(createServedDetail(order));

        when(orderRepository.findOrdersWithDetailsByDate(any(), any()))
                .thenReturn(List.of(order));

        List<KitchenTableOrderResponse> result =
                service.getPendingOrdersByTable();

        assertTrue(result.isEmpty());
    }

    @Test
    void getPendingOrdersByTable_MultipleTables() {

        Order order1 = createPaidOrder("B01");
        order1.getOrderDetails().add(createPendingDetail(order1));

        Order order2 = createPaidOrder("B02");
        order2.setIdOrder("HD002");
        order2.getOrderDetails().add(createPendingDetail(order2));

        when(orderRepository.findOrdersWithDetailsByDate(any(), any()))
                .thenReturn(List.of(order1, order2));

        List<KitchenTableOrderResponse> result =
                service.getPendingOrdersByTable();

        assertEquals(2, result.size());

        assertEquals("B01", result.get(0).getTableNumber());
        assertEquals("B02", result.get(1).getTableNumber());
    }
    //====================================================
    // getCompletedOrders
    //====================================================

    @Test
    void getCompletedOrders_Success() {

        Order order = createPaidOrder("B01");

        order.getOrderDetails().add(createServedDetail(order));

        when(orderRepository.findOrdersWithDetailsByDate(any(), any()))
                .thenReturn(List.of(order));

        List<KitchenTableOrderResponse> result =
                service.getCompletedOrders();

        assertEquals(1, result.size());

        assertEquals("B01",
                result.get(0).getTableNumber());

        assertEquals(1,
                result.get(0).getItems().size());

        assertEquals("Burger",
                result.get(0).getItems().get(0).getFoodName());
    }

    @Test
    void getCompletedOrders_OnlyPendingFood() {

        Order order = createPaidOrder("B01");

        order.getOrderDetails().add(createPendingDetail(order));

        when(orderRepository.findOrdersWithDetailsByDate(any(), any()))
                .thenReturn(List.of(order));

        List<KitchenTableOrderResponse> result =
                service.getCompletedOrders();

        assertTrue(result.isEmpty());
    }

    @Test
    void getCompletedOrders_NoOrders() {

        when(orderRepository.findOrdersWithDetailsByDate(any(), any()))
                .thenReturn(List.of());

        List<KitchenTableOrderResponse> result =
                service.getCompletedOrders();

        assertTrue(result.isEmpty());
    }

    //====================================================
    // getRemainingFoodSummary
    //====================================================

    @Test
    void getRemainingFoodSummary_Success() {

        Ingredient ingredient = new Ingredient();
        ingredient.setQuantityStock(BigDecimal.valueOf(20));

        FoodIngredient recipe = new FoodIngredient();
        recipe.setIngredient(ingredient);
        recipe.setQuantityUsed(BigDecimal.valueOf(2));

        Food food = createFood();
        food.setFoodIngredients(List.of(recipe));

        when(foodRepository.findAllWithIngredients())
                .thenReturn(List.of(food));

        var result = service.getRemainingFoodSummary();

        assertEquals(1, result.size());

        assertEquals("H001",
                result.get(0).getFoodId());

        assertEquals(10,
                result.get(0).getRemainingQuantity());
    }

    @Test
    void getRemainingFoodSummary_EmptyRepository() {

        when(foodRepository.findAllWithIngredients())
                .thenReturn(List.of());

        var result = service.getRemainingFoodSummary();

        assertTrue(result.isEmpty());
    }

    @Test
    void getRemainingFoodSummary_NoIngredient() {

        Food food = createFood();

        food.setFoodIngredients(new ArrayList<>());

        when(foodRepository.findAllWithIngredients())
                .thenReturn(List.of(food));

        var result = service.getRemainingFoodSummary();

        assertTrue(result.isEmpty());
    }

    @Test
    void getRemainingFoodSummary_IngredientNull() {

        FoodIngredient recipe = new FoodIngredient();

        recipe.setIngredient(null);
        recipe.setQuantityUsed(BigDecimal.ONE);

        Food food = createFood();
        food.setFoodIngredients(List.of(recipe));

        when(foodRepository.findAllWithIngredients())
                .thenReturn(List.of(food));

        var result = service.getRemainingFoodSummary();

        assertTrue(result.isEmpty());
    }

    @Test
    void getRemainingFoodSummary_StockNull() {

        Ingredient ingredient = new Ingredient();
        ingredient.setQuantityStock(null);

        FoodIngredient recipe = new FoodIngredient();
        recipe.setIngredient(ingredient);
        recipe.setQuantityUsed(BigDecimal.ONE);

        Food food = createFood();
        food.setFoodIngredients(List.of(recipe));

        when(foodRepository.findAllWithIngredients())
                .thenReturn(List.of(food));

        var result = service.getRemainingFoodSummary();

        assertTrue(result.isEmpty());
    }

    @Test
    void getRemainingFoodSummary_QuantityUsedZero() {

        Ingredient ingredient = new Ingredient();
        ingredient.setQuantityStock(BigDecimal.valueOf(100));

        FoodIngredient recipe = new FoodIngredient();
        recipe.setIngredient(ingredient);
        recipe.setQuantityUsed(BigDecimal.ZERO);

        Food food = createFood();
        food.setFoodIngredients(List.of(recipe));

        when(foodRepository.findAllWithIngredients())
                .thenReturn(List.of(food));

        var result = service.getRemainingFoodSummary();

        assertTrue(result.isEmpty());
    }

    @Test
    void getRemainingFoodSummary_MinPortionSelected() {

        Ingredient ingredient1 = new Ingredient();
        ingredient1.setQuantityStock(BigDecimal.valueOf(20));

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setQuantityStock(BigDecimal.valueOf(15));

        FoodIngredient recipe1 = new FoodIngredient();
        recipe1.setIngredient(ingredient1);
        recipe1.setQuantityUsed(BigDecimal.valueOf(2)); // 10 suất

        FoodIngredient recipe2 = new FoodIngredient();
        recipe2.setIngredient(ingredient2);
        recipe2.setQuantityUsed(BigDecimal.valueOf(3)); // 5 suất

        Food food = createFood();
        food.setFoodIngredients(List.of(recipe1, recipe2));

        when(foodRepository.findAllWithIngredients())
                .thenReturn(List.of(food));

        var result = service.getRemainingFoodSummary();

        assertEquals(1, result.size());

        assertEquals(5,
                result.get(0).getRemainingQuantity());
    }

    @Test
    void getRemainingFoodSummary_SortedDescending() {

        Ingredient ingredient1 = new Ingredient();
        ingredient1.setQuantityStock(BigDecimal.valueOf(30));

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setQuantityStock(BigDecimal.valueOf(10));

        FoodIngredient recipe1 = new FoodIngredient();
        recipe1.setIngredient(ingredient1);
        recipe1.setQuantityUsed(BigDecimal.ONE);

        FoodIngredient recipe2 = new FoodIngredient();
        recipe2.setIngredient(ingredient2);
        recipe2.setQuantityUsed(BigDecimal.ONE);

        Food food1 = createFood();
        food1.setIdFood("H001");
        food1.setFoodIngredients(List.of(recipe1));

        Food food2 = createFood();
        food2.setIdFood("H002");
        food2.setFoodIngredients(List.of(recipe2));

        when(foodRepository.findAllWithIngredients())
                .thenReturn(List.of(food2, food1));

        var result = service.getRemainingFoodSummary();

        assertEquals("H001",
                result.get(0).getFoodId());

        assertEquals(30,
                result.get(0).getRemainingQuantity());

        assertEquals("H002",
                result.get(1).getFoodId());

        assertEquals(10,
                result.get(1).getRemainingQuantity());
    }
    //====================================================
    // markOrderItemServed
    //====================================================

    @Test
    void markOrderItemServed_NotFound() {

        when(orderDetailRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> service.markOrderItemServed(1L)
        );

        assertEquals("Không tìm thấy chi tiết đơn hàng", ex.getMessage());

        verify(orderDetailRepository, never()).save(any());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void markOrderItemServed_AlreadyServed() {

        Order order = createPaidOrder("B01");

        OrderDetail detail = createServedDetail(order);

        when(orderDetailRepository.findById(2L))
                .thenReturn(Optional.of(detail));

        service.markOrderItemServed(2L);

        verify(orderDetailRepository, never()).save(any());
        verify(orderRepository, never()).save(any());
        verifyNoInteractions(messagingTemplate);
    }

    @Test
    void markOrderItemServed_OneItemServedButOrderNotFinished() {

        Order order = createPaidOrder("B01");

        OrderDetail detail1 = createPendingDetail(order);

        OrderDetail detail2 = createPendingDetail(order);
        detail2.setId(2L);

        order.setOrderDetails(
                new ArrayList<>(List.of(detail1, detail2))
        );

        when(orderDetailRepository.findById(1L))
                .thenReturn(Optional.of(detail1));

        service.markOrderItemServed(1L);

        assertEquals("SERVED", detail1.getStatus());

        verify(orderDetailRepository).save(detail1);

        verify(orderRepository, never()).save(any());

        verifyNoInteractions(messagingTemplate);
    }

    @Test
    void markOrderItemServed_AllItemsServed() {

        Order order = createPaidOrder("B01");

        OrderDetail detail1 = createPendingDetail(order);

        OrderDetail detail2 = createServedDetail(order);

        order.setOrderDetails(
                new ArrayList<>(List.of(detail1, detail2))
        );

        when(orderDetailRepository.findById(1L))
                .thenReturn(Optional.of(detail1));

        service.markOrderItemServed(1L);

        assertEquals("SERVED", detail1.getStatus());

        assertEquals("SERVED", order.getStatus());

        verify(orderDetailRepository).save(detail1);

        verify(orderRepository).save(order);

        verify(messagingTemplate)
                .convertAndSend(
                        eq("/topic/cashier"),
                        eq((Object) "TABLE_SERVED")
                );
    }

    @Test
    void markOrderItemServed_AllPendingBecomeServed() {

        Order order = createPaidOrder("A01");

        OrderDetail d1 = createPendingDetail(order);

        OrderDetail d2 = createPendingDetail(order);
        d2.setId(5L);

        order.setOrderDetails(
                new ArrayList<>(List.of(d1, d2))
        );

        when(orderDetailRepository.findById(1L))
                .thenReturn(Optional.of(d1));

        service.markOrderItemServed(1L);

        assertEquals("SERVED", d1.getStatus());

        assertEquals("PENDING", d2.getStatus());

        verify(orderRepository, never()).save(any());
    }

    @Test
    void markOrderItemServed_SaveCalledOnce() {

        Order order = createPaidOrder("A01");

        OrderDetail detail = createPendingDetail(order);

        order.setOrderDetails(
                new ArrayList<>(List.of(detail))
        );

        when(orderDetailRepository.findById(1L))
                .thenReturn(Optional.of(detail));

        service.markOrderItemServed(1L);

        verify(orderDetailRepository, times(1))
                .save(detail);
    }

    @Test
    void markOrderItemServed_OrderRepositoryCalledOnce() {

        Order order = createPaidOrder("A01");

        OrderDetail detail = createPendingDetail(order);

        order.setOrderDetails(
                new ArrayList<>(List.of(detail))
        );

        when(orderDetailRepository.findById(1L))
                .thenReturn(Optional.of(detail));

        service.markOrderItemServed(1L);

        verify(orderRepository, times(1))
                .save(order);
    }

    @Test
    void markOrderItemServed_WebsocketCalledOnce() {

        Order order = createPaidOrder("A01");

        OrderDetail detail = createPendingDetail(order);

        order.setOrderDetails(
                new ArrayList<>(List.of(detail))
        );

        when(orderDetailRepository.findById(1L))
                .thenReturn(Optional.of(detail));

        service.markOrderItemServed(1L);

        verify(messagingTemplate, times(1))
                .convertAndSend(
                        eq("/topic/cashier"),
                        eq((Object) "TABLE_SERVED")
                );
    }
    //====================================================
    // Extra branch coverage
    //====================================================

    @Test
    void getPendingOrdersByTable_FirstOrderTimeIsEarliest() {

        Order order1 = createPaidOrder("B01");
        order1.setOrderTime(LocalDateTime.now().minusMinutes(30));

        Order order2 = createPaidOrder("B01");
        order2.setIdOrder("HD002");
        order2.setOrderTime(LocalDateTime.now().minusMinutes(10));

        order1.getOrderDetails().add(createPendingDetail(order1));
        order2.getOrderDetails().add(createPendingDetail(order2));

        when(orderRepository.findOrdersWithDetailsByDate(any(), any()))
                .thenReturn(List.of(order2, order1));

        List<KitchenTableOrderResponse> result =
                service.getPendingOrdersByTable();

        assertEquals(1, result.size());

        assertEquals(order1.getOrderTime(),
                result.get(0).getFirstOrderTime());
    }

    @Test
    void getCompletedOrders_SortedNewestFirst() {

        Order oldOrder = createPaidOrder("B01");
        oldOrder.setOrderTime(LocalDateTime.now().minusMinutes(30));
        oldOrder.getOrderDetails().add(createServedDetail(oldOrder));

        Order newOrder = createPaidOrder("B02");
        newOrder.setIdOrder("HD002");
        newOrder.setOrderTime(LocalDateTime.now().minusMinutes(5));
        newOrder.getOrderDetails().add(createServedDetail(newOrder));

        when(orderRepository.findOrdersWithDetailsByDate(any(), any()))
                .thenReturn(List.of(oldOrder, newOrder));

        List<KitchenTableOrderResponse> result =
                service.getCompletedOrders();

        assertEquals("B02", result.get(0).getTableNumber());
        assertEquals("B01", result.get(1).getTableNumber());
    }

    @Test
    void getRemainingFoodSummary_NullFoodIngredientList() {

        Food food = createFood();
        food.setFoodIngredients(null);

        when(foodRepository.findAllWithIngredients())
                .thenReturn(List.of(food));

        assertTrue(service.getRemainingFoodSummary().isEmpty());
    }

    @Test
    void getRemainingFoodSummary_NegativeStock() {

        Ingredient ingredient = new Ingredient();
        ingredient.setQuantityStock(BigDecimal.valueOf(-5));

        FoodIngredient fi = new FoodIngredient();
        fi.setIngredient(ingredient);
        fi.setQuantityUsed(BigDecimal.ONE);

        Food food = createFood();
        food.setFoodIngredients(List.of(fi));

        when(foodRepository.findAllWithIngredients())
                .thenReturn(List.of(food));

        assertTrue(service.getRemainingFoodSummary().isEmpty());
    }

    @Test
    void getRemainingFoodSummary_DecimalDivision() {

        Ingredient ingredient = new Ingredient();
        ingredient.setQuantityStock(BigDecimal.valueOf(10));

        FoodIngredient fi = new FoodIngredient();
        fi.setIngredient(ingredient);
        fi.setQuantityUsed(BigDecimal.valueOf(3));

        Food food = createFood();
        food.setFoodIngredients(List.of(fi));

        when(foodRepository.findAllWithIngredients())
                .thenReturn(List.of(food));

        var result = service.getRemainingFoodSummary();

        assertEquals(3, result.get(0).getRemainingQuantity());
    }

    @Test
    void markOrderItemServed_OrderAlreadyServedStatus() {

        Order order = createPaidOrder("B01");
        order.setStatus("SERVED");

        OrderDetail detail = createPendingDetail(order);
        order.setOrderDetails(new ArrayList<>(List.of(detail)));

        when(orderDetailRepository.findById(1L))
                .thenReturn(Optional.of(detail));

        service.markOrderItemServed(1L);

        verify(orderRepository).save(order);
    }

    @Test
    void getPendingOrdersByTable_ElapsedMinutesNeverNegative() {

        Order order = createPaidOrder("B01");
        order.setOrderTime(LocalDateTime.now().plusMinutes(5));

        order.getOrderDetails().add(createPendingDetail(order));

        when(orderRepository.findOrdersWithDetailsByDate(any(), any()))
                .thenReturn(List.of(order));

        var result = service.getPendingOrdersByTable();

        assertEquals(0, result.get(0).getElapsedMinutes());
    }

    @Test
    void getRemainingFoodSummary_MultipleFoods_FilterZeroQuantity() {

        Ingredient ingredient = new Ingredient();
        ingredient.setQuantityStock(BigDecimal.valueOf(10));

        FoodIngredient ok = new FoodIngredient();
        ok.setIngredient(ingredient);
        ok.setQuantityUsed(BigDecimal.ONE);

        Food food1 = createFood();
        food1.setIdFood("H001");
        food1.setFoodIngredients(List.of(ok));

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setQuantityStock(BigDecimal.ZERO);

        FoodIngredient zero = new FoodIngredient();
        zero.setIngredient(ingredient2);
        zero.setQuantityUsed(BigDecimal.ONE);

        Food food2 = createFood();
        food2.setIdFood("H002");
        food2.setFoodIngredients(List.of(zero));

        when(foodRepository.findAllWithIngredients())
                .thenReturn(List.of(food1, food2));

        var result = service.getRemainingFoodSummary();

        assertEquals(1, result.size());
        assertEquals("H001", result.get(0).getFoodId());
    }

}