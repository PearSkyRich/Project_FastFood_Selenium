package com.fastfood.service.impl;

import com.fastfood.dto.request.OrderRequest;
import com.fastfood.dto.request.PaymentRequest;
import com.fastfood.dto.response.CashierOrderDetailResponse;
import com.fastfood.dto.response.CashierPaymentResponse;
import com.fastfood.entity.catalog.Food;
import com.fastfood.entity.catalog.FoodIngredient;
import com.fastfood.entity.catalog.Ingredient;
import com.fastfood.entity.system.Role;
import com.fastfood.entity.system.User;
import com.fastfood.entity.transaction.Order;
import com.fastfood.entity.transaction.OrderDetail;
import com.fastfood.repository.FoodRepository;
import com.fastfood.repository.IngredientRepository;
import com.fastfood.repository.OrderRepository;
import com.fastfood.repository.SalesInvoiceRepository;
import com.fastfood.repository.UserRepository;
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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SalesServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private FoodRepository foodRepository;

    @Mock
    private SalesInvoiceRepository salesInvoiceRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private SalesServiceImpl service;

    //====================================================
    // Helper
    //====================================================

    private Order createOrder(String id, String table, String status) {

        Order order = new Order();

        order.setIdOrder(id);
        order.setTableNumber(table);
        order.setStatus(status);
        order.setOrderTime(LocalDateTime.now());

        return order;
    }

    private User createTableUser(String username, String fullName) {

        Role role = new Role();
        role.setRoleName("Bàn");

        User user = new User();
        user.setUsername(username);
        user.setFullName(fullName);
        user.setRole(role);

        return user;
    }

    //====================================================
    // generateNextOrderId
    //====================================================

    @Test
    void generateNextOrderId_FirstOrder() {

        when(orderRepository.findMaxIdOrder())
                .thenReturn(null);

        String id = service.generateNextOrderId();

        assertEquals("ORD001", id);
    }


    @Test
    void generateNextOrderId_ThreeDigits() {

        when(orderRepository.findMaxIdOrder())
                .thenReturn("ORD199");

        String id = service.generateNextOrderId();

        assertEquals("ORD200", id);
    }

    //====================================================
    // generateNextInvoiceId
    //====================================================

    @Test
    void generateNextInvoiceId_FirstInvoice() {

        when(salesInvoiceRepository.findMaxIdInvoice())
                .thenReturn(null);

        String id = service.generateNextInvoiceId();

        assertEquals("INV001", id);
    }

    @Test
    void generateNextInvoiceId_NextInvoice() {

        when(salesInvoiceRepository.findMaxIdInvoice())
                .thenReturn("INV015");

        String id = service.generateNextInvoiceId();

        assertEquals("INV016", id);
    }

    @Test
    void generateNextInvoiceId_ThreeDigits() {

        when(salesInvoiceRepository.findMaxIdInvoice())
                .thenReturn("INV199");

        String id = service.generateNextInvoiceId();

        assertEquals("INV200", id);
    }

    //====================================================
    // getOccupiedTableNumbers
    //====================================================

    @Test
    void getOccupiedTableNumbers_Success() {

        Order o1 = createOrder("ORD001", "Bàn N01", "PENDING");
        Order o2 = createOrder("ORD002", "N02", "PENDING");
        Order o3 = createOrder("ORD003", "BAN N03", "PENDING");

        when(orderRepository.findByStatus("PENDING"))
                .thenReturn(List.of(o1, o2, o3));

        Set<String> result = service.getOccupiedTableNumbers();

        assertEquals(3, result.size());

        assertTrue(result.contains("N01"));
        assertTrue(result.contains("N02"));
        assertTrue(result.contains("N03"));
    }

    @Test
    void getOccupiedTableNumbers_Empty() {

        when(orderRepository.findByStatus("PENDING"))
                .thenReturn(List.of());

        Set<String> result = service.getOccupiedTableNumbers();

        assertTrue(result.isEmpty());
    }

    @Test
    void getOccupiedTableNumbers_RemoveDuplicate() {

        Order o1 = createOrder("ORD001", "N01", "PENDING");
        Order o2 = createOrder("ORD002", "Bàn N01", "PENDING");

        when(orderRepository.findByStatus("PENDING"))
                .thenReturn(List.of(o1, o2));

        Set<String> result = service.getOccupiedTableNumbers();

        assertEquals(1, result.size());

        assertTrue(result.contains("N01"));
    }

    @Test
    void getOccupiedTableNumbers_Mixed() {

        Order o1 = new Order();
        o1.setTableNumber(null);

        Order o2 = new Order();
        o2.setTableNumber("N05");

        when(orderRepository.findByStatus("PENDING"))
                .thenReturn(List.of(o1,o2));

        Set<String> result =
                service.getOccupiedTableNumbers();

        assertEquals(1,result.size());
        assertTrue(result.contains("N05"));
    }
    //====================================================
    // getTableStatuses
    //====================================================
    @Test
    void getTableStatuses_ServedOnly(){

        User user=createTableUser("ban01","N01");

        when(userRepository.findAllWithRole())
                .thenReturn(List.of(user));

        Order served=createOrder("ORD001","N01","SERVED");

        when(orderRepository.findByStatus("PENDING"))
                .thenReturn(List.of());

        when(orderRepository.findByStatus("PAID"))
                .thenReturn(List.of());

        when(orderRepository.findByStatus("SERVED"))
                .thenReturn(List.of(served));

        var result=service.getTableStatuses();

        assertEquals("SERVED",
                result.get(0).getStatus());
    }
    @Test
    void getTableStatuses_AllStatus() {

        User u1 = createTableUser("ban01", "N01");
        User u2 = createTableUser("ban02", "N02");
        User u3 = createTableUser("ban03", "N03");

        when(userRepository.findAllWithRole())
                .thenReturn(List.of(u1, u2, u3));

        Order pending = createOrder("ORD001", "N01", "PENDING");
        Order paid = createOrder("ORD002", "N02", "PAID");

        when(orderRepository.findByStatus("PENDING"))
                .thenReturn(List.of(pending));

        when(orderRepository.findByStatus("PAID"))
                .thenReturn(List.of(paid));

        when(orderRepository.findByStatus("SERVED"))
                .thenReturn(List.of());

        var result = service.getTableStatuses();

        assertEquals(3, result.size());

        assertEquals("PENDING", result.get(0).getStatus());
        assertEquals("PAID", result.get(1).getStatus());
        assertEquals("EMPTY", result.get(2).getStatus());
    }

    @Test
    void getTableStatuses_LatestOrderWins() {

        User user = createTableUser("ban01", "N01");

        when(userRepository.findAllWithRole())
                .thenReturn(List.of(user));

        Order oldOrder = createOrder("ORD001", "N01", "PENDING");
        oldOrder.setOrderTime(LocalDateTime.now().minusHours(2));

        Order newOrder = createOrder("ORD002", "N01", "SERVED");
        newOrder.setOrderTime(LocalDateTime.now());

        when(orderRepository.findByStatus("PENDING"))
                .thenReturn(List.of(oldOrder));

        when(orderRepository.findByStatus("PAID"))
                .thenReturn(List.of());

        when(orderRepository.findByStatus("SERVED"))
                .thenReturn(List.of(newOrder));

        var result = service.getTableStatuses();

        assertEquals(1, result.size());
        assertEquals("SERVED", result.get(0).getStatus());
    }

    @Test
    void getTableStatuses_NoOrders() {

        User user = createTableUser("ban01", "N01");

        when(userRepository.findAllWithRole())
                .thenReturn(List.of(user));

        when(orderRepository.findByStatus(anyString()))
                .thenReturn(List.of());

        var result = service.getTableStatuses();

        assertEquals(1, result.size());
        assertEquals("EMPTY", result.get(0).getStatus());
    }

    @Test
    void getTableStatuses_UnpaidFlag() {

        User user = createTableUser("ban01", "N01");

        when(userRepository.findAllWithRole())
                .thenReturn(List.of(user));

        Order pending = createOrder("ORD001", "N01", "PENDING");

        when(orderRepository.findByStatus("PENDING"))
                .thenReturn(List.of(pending));

        when(orderRepository.findByStatus("PAID"))
                .thenReturn(List.of());

        when(orderRepository.findByStatus("SERVED"))
                .thenReturn(List.of());

        var result = service.getTableStatuses();

        assertTrue(result.get(0).isUnpaid());
    }

    //====================================================
    // getPendingOrderByTable
    //====================================================
    @Test
    void getPendingOrderByTable_NullQuantity(){

        Food food=new Food();
        food.setFoodName("Burger");
        food.setUnitPrice(BigDecimal.TEN);

        OrderDetail detail=new OrderDetail();
        detail.setFood(food);
        detail.setQuantity(null);
        detail.setUnitPrice(BigDecimal.TEN);

        Order order=createOrder("ORD001","N01","PENDING");
        order.setOrderDetails(List.of(detail));

        when(orderRepository.findByStatus("PENDING"))
                .thenReturn(List.of(order));

        when(orderRepository.findByStatus("PAID"))
                .thenReturn(List.of());

        when(orderRepository.findByStatus("SERVED"))
                .thenReturn(List.of());

        CashierOrderDetailResponse response=
                service.getPendingOrderByTable("N01");

        assertEquals(BigDecimal.ZERO,
                response.getTotalAmount());
    }
    @Test
    void getPendingOrderByTable_Success() {

        Food food = new Food();
        food.setIdFood("H001");
        food.setFoodName("Gà rán");
        food.setImageUrlFood("ga.png");
        food.setUnitPrice(BigDecimal.valueOf(50000));

        OrderDetail detail = new OrderDetail();
        detail.setId(1L);
        detail.setFood(food);
        detail.setQuantity(2);
        detail.setUnitPrice(BigDecimal.valueOf(50000));

        Order order = createOrder("ORD001", "N01", "PENDING");
        order.setCustomerName("Khách A");
        order.setOrderDetails(List.of(detail));

        detail.setOrder(order);

        when(orderRepository.findByStatus("PENDING"))
                .thenReturn(List.of(order));

        when(orderRepository.findByStatus("PAID"))
                .thenReturn(List.of());

        when(orderRepository.findByStatus("SERVED"))
                .thenReturn(List.of());

        var result = service.getPendingOrderByTable("N01");

        assertEquals("ORD001", result.getOrderId());
        assertEquals("N01", result.getTableNumber());

        assertEquals(1, result.getItems().size());

        assertEquals(
                BigDecimal.valueOf(100000),
                result.getTotalAmount()
        );
    }

    @Test
    void getPendingOrderByTable_OrderNotFound() {

        when(orderRepository.findByStatus(anyString()))
                .thenReturn(List.of());

        assertThrows(RuntimeException.class,
                () -> service.getPendingOrderByTable("N99"));
    }

    @Test
    void getPendingOrderByTable_IgnoreNullFood() {

        OrderDetail detail = new OrderDetail();
        detail.setId(1L);
        detail.setFood(null);

        Order order = createOrder("ORD001", "N01", "PENDING");
        order.setOrderDetails(List.of(detail));

        when(orderRepository.findByStatus("PENDING"))
                .thenReturn(List.of(order));

        when(orderRepository.findByStatus("PAID"))
                .thenReturn(List.of());

        when(orderRepository.findByStatus("SERVED"))
                .thenReturn(List.of());

        var result = service.getPendingOrderByTable("N01");

        assertTrue(result.getItems().isEmpty());
        assertEquals(BigDecimal.ZERO, result.getTotalAmount());
    }

    @Test
    void getPendingOrderByTable_OrderDetailsSortedById() {

        Food food = new Food();
        food.setIdFood("H001");
        food.setFoodName("Burger");
        food.setUnitPrice(BigDecimal.valueOf(30000));

        OrderDetail d2 = new OrderDetail();
        d2.setId(2L);
        d2.setFood(food);
        d2.setQuantity(1);
        d2.setUnitPrice(BigDecimal.valueOf(30000));

        OrderDetail d1 = new OrderDetail();
        d1.setId(1L);
        d1.setFood(food);
        d1.setQuantity(1);
        d1.setUnitPrice(BigDecimal.valueOf(30000));

        Order order = createOrder("ORD001", "N01", "PENDING");
        order.setOrderDetails(List.of(d2, d1));

        when(orderRepository.findByStatus("PENDING"))
                .thenReturn(List.of(order));

        when(orderRepository.findByStatus("PAID"))
                .thenReturn(List.of());

        when(orderRepository.findByStatus("SERVED"))
                .thenReturn(List.of());

        var result = service.getPendingOrderByTable("N01");

        assertEquals(1L,
                result.getItems().get(0).getOrderDetailId());
    }

    @Test
    void getPendingOrderByTable_ChooseLatestOrderAmongStatuses() {

        Order oldOrder = createOrder("ORD001", "N01", "PENDING");
        oldOrder.setOrderTime(LocalDateTime.now().minusHours(2));
        oldOrder.setOrderDetails(new ArrayList<>());

        Order newOrder = createOrder("ORD002", "N01", "PAID");
        newOrder.setOrderTime(LocalDateTime.now());
        newOrder.setOrderDetails(new ArrayList<>());

        when(orderRepository.findByStatus("PENDING"))
                .thenReturn(List.of(oldOrder));

        when(orderRepository.findByStatus("PAID"))
                .thenReturn(List.of(newOrder));

        when(orderRepository.findByStatus("SERVED"))
                .thenReturn(List.of());

        var result = service.getPendingOrderByTable("N01");

        assertEquals("ORD002", result.getOrderId());
    }
    //====================================================
    // placeOrder
    //====================================================

    @Test
    void placeOrder_CreateNewOrder_Success() {

        // Account bàn
        User tableUser = createTableUser("ban01", "N01");

        when(userRepository.findAllWithRole())
                .thenReturn(List.of(tableUser));

        // Chưa có đơn PENDING
        when(orderRepository.findByStatus("PENDING"))
                .thenReturn(new ArrayList<>());

        when(orderRepository.findMaxIdOrder())
                .thenReturn("ORD009");

        // Ingredient
        Ingredient ingredient = new Ingredient();
        ingredient.setIdIngredient("NL001");
        ingredient.setQuantityStock(BigDecimal.valueOf(20));

        FoodIngredient recipe = new FoodIngredient();
        recipe.setIngredient(ingredient);
        recipe.setQuantityUsed(BigDecimal.valueOf(2));

        // Food
        Food food = new Food();
        food.setIdFood("H001");
        food.setFoodName("Burger");
        food.setUnitPrice(BigDecimal.valueOf(50000));
        food.setFoodIngredients(List.of(recipe));

        when(foodRepository.findById("H001"))
                .thenReturn(Optional.of(food));

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Request
        OrderRequest request = new OrderRequest();
        request.setTableNumber("N01");
        request.setCustomerName("Khách A");
        request.setCreatedBy("cashier");

        OrderRequest.OrderItemDto item = new OrderRequest.OrderItemDto();
        item.setFoodId("H001");
        item.setQuantity(2);

        request.setItems(List.of(item));

        Order saved = service.placeOrder(request);

        assertEquals("ORD010", saved.getIdOrder());
        assertEquals("PENDING", saved.getStatus());
        assertEquals(1, saved.getOrderDetails().size());

        verify(orderRepository).save(any(Order.class));
        verify(ingredientRepository).save(any(Ingredient.class));
        verify(messagingTemplate)
                .convertAndSend("/topic/cashier", "NEW_ORDER_PENDING");
    }


    @Test
    void placeOrder_InvalidTable() {

        when(userRepository.findAllWithRole())
                .thenReturn(List.of());

        OrderRequest request = new OrderRequest();
        request.setTableNumber("N99");

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> service.placeOrder(request));

        assertTrue(ex.getMessage().contains("Bàn không hợp lệ"));
    }

    @Test
    void placeOrder_BlankTable() {

        User tableUser = createTableUser("ban01", "N01");

        when(userRepository.findAllWithRole())
                .thenReturn(List.of(tableUser));

        OrderRequest request = new OrderRequest();
        request.setTableNumber("");

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> service.placeOrder(request));

        assertTrue(ex.getMessage().contains("Bàn không hợp lệ"));
    }

    @Test
    void placeOrder_FoodNotFound() {

        User tableUser = createTableUser("ban01", "N01");

        when(userRepository.findAllWithRole())
                .thenReturn(List.of(tableUser));

        when(orderRepository.findByStatus("PENDING"))
                .thenReturn(new ArrayList<>());

        when(foodRepository.findById("H001"))
                .thenReturn(Optional.empty());

        OrderRequest request = new OrderRequest();
        request.setTableNumber("N01");

        OrderRequest.OrderItemDto item = new OrderRequest.OrderItemDto();
        item.setFoodId("H001");
        item.setQuantity(1);

        request.setItems(List.of(item));

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> service.placeOrder(request));

        assertTrue(ex.getMessage().contains("Không tìm thấy món ăn"));
    }

    @Test
    void placeOrder_OutOfStock() {

        User tableUser = createTableUser("ban01", "N01");

        when(userRepository.findAllWithRole())
                .thenReturn(List.of(tableUser));

        when(orderRepository.findByStatus("PENDING"))
                .thenReturn(new ArrayList<>());

        Ingredient ingredient = new Ingredient();
        ingredient.setIdIngredient("NL001");
        ingredient.setQuantityStock(BigDecimal.ONE);

        FoodIngredient recipe = new FoodIngredient();
        recipe.setIngredient(ingredient);
        recipe.setQuantityUsed(BigDecimal.valueOf(2));

        Food food = new Food();
        food.setIdFood("H001");
        food.setFoodName("Burger");
        food.setUnitPrice(BigDecimal.valueOf(50000));
        food.setFoodIngredients(List.of(recipe));

        when(foodRepository.findById("H001"))
                .thenReturn(Optional.of(food));

        OrderRequest request = new OrderRequest();
        request.setTableNumber("N01");

        OrderRequest.OrderItemDto item = new OrderRequest.OrderItemDto();
        item.setFoodId("H001");
        item.setQuantity(1);

        request.setItems(List.of(item));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.placeOrder(request));

        assertTrue(ex.getMessage().contains("HẾT_HÀNG"));
    }

    @Test
    void placeOrder_MultipleIngredients() {

        User tableUser = createTableUser("ban01", "N01");

        when(userRepository.findAllWithRole())
                .thenReturn(List.of(tableUser));

        when(orderRepository.findByStatus("PENDING"))
                .thenReturn(new ArrayList<>());

        when(orderRepository.findMaxIdOrder())
                .thenReturn("ORD001");

        Ingredient i1 = new Ingredient();
        i1.setQuantityStock(BigDecimal.valueOf(10));

        Ingredient i2 = new Ingredient();
        i2.setQuantityStock(BigDecimal.valueOf(20));

        FoodIngredient f1 = new FoodIngredient();
        f1.setIngredient(i1);
        f1.setQuantityUsed(BigDecimal.ONE);

        FoodIngredient f2 = new FoodIngredient();
        f2.setIngredient(i2);
        f2.setQuantityUsed(BigDecimal.valueOf(2));

        Food food = new Food();
        food.setIdFood("H001");
        food.setFoodIngredients(List.of(f1, f2));
        food.setUnitPrice(BigDecimal.valueOf(50000));

        when(foodRepository.findById("H001"))
                .thenReturn(Optional.of(food));

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(i -> i.getArgument(0));

        OrderRequest request = new OrderRequest();
        request.setTableNumber("N01");

        OrderRequest.OrderItemDto item = new OrderRequest.OrderItemDto();
        item.setFoodId("H001");
        item.setQuantity(2);

        request.setItems(List.of(item));

        service.placeOrder(request);

        assertEquals(BigDecimal.valueOf(8), i1.getQuantityStock());
        assertEquals(BigDecimal.valueOf(16), i2.getQuantityStock());

        verify(ingredientRepository, times(2)).save(any(Ingredient.class));
    }

    @Test
    void placeOrder_MultipleFoods() {

        User tableUser = createTableUser("ban01", "N01");

        when(userRepository.findAllWithRole())
                .thenReturn(List.of(tableUser));

        when(orderRepository.findByStatus("PENDING"))
                .thenReturn(new ArrayList<>());

        when(orderRepository.findMaxIdOrder())
                .thenReturn("ORD001");

        Food food = new Food();
        food.setIdFood("H001");
        food.setFoodIngredients(new ArrayList<>());
        food.setUnitPrice(BigDecimal.valueOf(30000));

        when(foodRepository.findById(anyString()))
                .thenReturn(Optional.of(food));

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(i -> i.getArgument(0));

        OrderRequest.OrderItemDto i1 = new OrderRequest.OrderItemDto();
        i1.setFoodId("H001");
        i1.setQuantity(1);

        OrderRequest.OrderItemDto i2 = new OrderRequest.OrderItemDto();
        i2.setFoodId("H002");
        i2.setQuantity(2);

        OrderRequest request = new OrderRequest();
        request.setTableNumber("N01");
        request.setItems(List.of(i1, i2));

        Order result = service.placeOrder(request);

        assertEquals(2, result.getOrderDetails().size());
    }

    @Test
    void placeOrder_AddToExistingPendingOrder() {

        User tableUser = createTableUser("ban01", "N01");

        when(userRepository.findAllWithRole()).thenReturn(List.of(tableUser));

        Order existingOrder = new Order();
        existingOrder.setIdOrder("ORD001");
        existingOrder.setTableNumber("N01");
        existingOrder.setStatus("PENDING");
        existingOrder.setOrderDetails(new ArrayList<>());

        when(orderRepository.findByStatus("PENDING"))
                .thenReturn(List.of(existingOrder));

        Food food = new Food();
        food.setIdFood("H001");
        food.setUnitPrice(BigDecimal.valueOf(50000));
        food.setFoodIngredients(new ArrayList<>());

        when(foodRepository.findById("H001"))
                .thenReturn(Optional.of(food));

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        OrderRequest request = new OrderRequest();
        request.setTableNumber("N01");

        OrderRequest.OrderItemDto item = new OrderRequest.OrderItemDto();
        item.setFoodId("H001");
        item.setQuantity(2);

        request.setItems(List.of(item));

        Order result = service.placeOrder(request);

        assertEquals("ORD001", result.getIdOrder());
        assertEquals(1, result.getOrderDetails().size());

        verify(orderRepository, never()).findMaxIdOrder();
    }

    @Test
    void placeOrder_IngredientStockExactlyEnough() {

        User tableUser = createTableUser("ban01", "N01");

        when(userRepository.findAllWithRole()).thenReturn(List.of(tableUser));
        when(orderRepository.findByStatus("PENDING")).thenReturn(new ArrayList<>());
        when(orderRepository.findMaxIdOrder()).thenReturn("ORD001");

        Ingredient ingredient = new Ingredient();
        ingredient.setQuantityStock(BigDecimal.valueOf(4));

        FoodIngredient recipe = new FoodIngredient();
        recipe.setIngredient(ingredient);
        recipe.setQuantityUsed(BigDecimal.valueOf(2));

        Food food = new Food();
        food.setIdFood("H001");
        food.setUnitPrice(BigDecimal.valueOf(60000));
        food.setFoodIngredients(List.of(recipe));

        when(foodRepository.findById("H001"))
                .thenReturn(Optional.of(food));

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        OrderRequest request = new OrderRequest();
        request.setTableNumber("N01");

        OrderRequest.OrderItemDto item = new OrderRequest.OrderItemDto();
        item.setFoodId("H001");
        item.setQuantity(2);

        request.setItems(List.of(item));

        service.placeOrder(request);

        assertEquals(BigDecimal.ZERO, ingredient.getQuantityStock());
    }

    @Test
    void placeOrder_NoIngredients() {

        User tableUser = createTableUser("ban01", "N01");

        when(userRepository.findAllWithRole()).thenReturn(List.of(tableUser));
        when(orderRepository.findByStatus("PENDING")).thenReturn(new ArrayList<>());
        when(orderRepository.findMaxIdOrder()).thenReturn("ORD001");

        Food food = new Food();
        food.setIdFood("H001");
        food.setUnitPrice(BigDecimal.valueOf(30000));
        food.setFoodIngredients(new ArrayList<>());

        when(foodRepository.findById("H001"))
                .thenReturn(Optional.of(food));

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        OrderRequest request = new OrderRequest();
        request.setTableNumber("N01");

        OrderRequest.OrderItemDto item = new OrderRequest.OrderItemDto();
        item.setFoodId("H001");
        item.setQuantity(1);

        request.setItems(List.of(item));

        service.placeOrder(request);

        verify(ingredientRepository, never()).save(any());
    }

    @Test
    void placeOrder_IngredientNullStock() {

        User tableUser = createTableUser("ban01", "N01");

        when(userRepository.findAllWithRole()).thenReturn(List.of(tableUser));
        when(orderRepository.findByStatus("PENDING")).thenReturn(new ArrayList<>());

        Ingredient ingredient = new Ingredient();
        ingredient.setQuantityStock(null);

        FoodIngredient recipe = new FoodIngredient();
        recipe.setIngredient(ingredient);
        recipe.setQuantityUsed(BigDecimal.ONE);

        Food food = new Food();
        food.setIdFood("H001");
        food.setFoodName("Pizza");
        food.setUnitPrice(BigDecimal.valueOf(50000));
        food.setFoodIngredients(List.of(recipe));

        when(foodRepository.findById("H001"))
                .thenReturn(Optional.of(food));

        OrderRequest request = new OrderRequest();
        request.setTableNumber("N01");

        OrderRequest.OrderItemDto item = new OrderRequest.OrderItemDto();
        item.setFoodId("H001");
        item.setQuantity(1);

        request.setItems(List.of(item));

        assertThrows(IllegalArgumentException.class,
                () -> service.placeOrder(request));
    }

    @Test
    void placeOrder_IngredientNullQuantityUsed() {

        User tableUser = createTableUser("ban01", "N01");

        when(userRepository.findAllWithRole()).thenReturn(List.of(tableUser));
        when(orderRepository.findByStatus("PENDING")).thenReturn(new ArrayList<>());
        when(orderRepository.findMaxIdOrder()).thenReturn("ORD001");

        Ingredient ingredient = new Ingredient();
        ingredient.setQuantityStock(BigDecimal.TEN);

        FoodIngredient recipe = new FoodIngredient();
        recipe.setIngredient(ingredient);
        recipe.setQuantityUsed(null);

        Food food = new Food();
        food.setIdFood("H001");
        food.setUnitPrice(BigDecimal.valueOf(50000));
        food.setFoodIngredients(List.of(recipe));

        when(foodRepository.findById("H001"))
                .thenReturn(Optional.of(food));

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        OrderRequest request = new OrderRequest();
        request.setTableNumber("N01");

        OrderRequest.OrderItemDto item = new OrderRequest.OrderItemDto();
        item.setFoodId("H001");
        item.setQuantity(5);

        request.setItems(List.of(item));

        service.placeOrder(request);

        assertEquals(BigDecimal.TEN, ingredient.getQuantityStock());
    }
    @Test
    void processPayment_EmptyOrder() {

        Order order = new Order();
        order.setIdOrder("ORD001");
        order.setStatus("PENDING");
        order.setTableNumber("N01");
        order.setOrderDetails(new ArrayList<>());

        when(orderRepository.findById("ORD001"))
                .thenReturn(Optional.of(order));

        when(salesInvoiceRepository.findMaxIdInvoice())
                .thenReturn(null);

        when(salesInvoiceRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        when(orderRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        PaymentRequest request = new PaymentRequest();
        request.setOrderId("ORD001");

        CashierPaymentResponse response =
                service.processPayment(request);

        assertEquals(BigDecimal.ZERO,
                response.getTotalAmount());
    }
    @Test
    void processPayment_Success() {

        Order order = new Order();
        order.setIdOrder("ORD001");
        order.setTableNumber("N01");
        order.setStatus("PENDING");

        OrderDetail detail = new OrderDetail();
        detail.setQuantity(2);
        detail.setUnitPrice(BigDecimal.valueOf(50000));

        order.setOrderDetails(List.of(detail));

        when(orderRepository.findById("ORD001"))
                .thenReturn(Optional.of(order));

        when(salesInvoiceRepository.findMaxIdInvoice())
                .thenReturn("INV001");

        when(salesInvoiceRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        when(orderRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        PaymentRequest request = new PaymentRequest();
        request.setOrderId("ORD001");
        request.setCustomerPhone("0123456789");
        request.setPaymentMethod("CASH");

        CashierPaymentResponse response = service.processPayment(request);

        assertEquals("INV002", response.getInvoiceId());
        assertEquals("ORD001", response.getOrderId());
        assertEquals(BigDecimal.valueOf(100000), response.getTotalAmount());

        verify(salesInvoiceRepository).save(any());
        verify(orderRepository).save(order);
        verify(messagingTemplate).convertAndSend("/topic/kitchen", "NEW_ORDER");
    }

    @Test
    void processPayment_OrderNotFound() {

        when(orderRepository.findById("ORD001"))
                .thenReturn(Optional.empty());

        PaymentRequest request = new PaymentRequest();
        request.setOrderId("ORD001");

        assertThrows(RuntimeException.class,
                () -> service.processPayment(request));
    }

    @Test
    void processPayment_AlreadyPaid() {

        Order order = new Order();
        order.setStatus("PAID");

        when(orderRepository.findById("ORD001"))
                .thenReturn(Optional.of(order));

        PaymentRequest request = new PaymentRequest();
        request.setOrderId("ORD001");

        assertThrows(RuntimeException.class,
                () -> service.processPayment(request));
    }

    @Test
    void completeOrder_AlreadyCompleted() {

        Order order = new Order();
        order.setStatus("COMPLETED");

        when(orderRepository.findById("ORD001"))
                .thenReturn(Optional.of(order));

        assertThrows(RuntimeException.class,
                () -> service.completeOrder("ORD001"));

        verify(orderRepository, never()).save(any());
    }
    @Test
    void completeOrder_Success() {

        Order order = new Order();
        order.setIdOrder("ORD001");
        order.setStatus("SERVED");

        when(orderRepository.findById("ORD001"))
                .thenReturn(Optional.of(order));

        service.completeOrder("ORD001");

        assertEquals("COMPLETED", order.getStatus());

        verify(orderRepository).save(order);

        verify(messagingTemplate)
                .convertAndSend("/topic/cashier",
                        "TABLE_CLEARED");
    }

    @Test
    void completeOrder_OrderNotFound() {

        when(orderRepository.findById("ORD001"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.completeOrder("ORD001"));
    }

    @Test
    void completeOrder_StatusNotServed() {

        Order order = new Order();
        order.setStatus("PENDING");

        when(orderRepository.findById("ORD001"))
                .thenReturn(Optional.of(order));

        assertThrows(RuntimeException.class,
                () -> service.completeOrder("ORD001"));

        verify(orderRepository, never()).save(any());
    }




}