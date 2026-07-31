package com.fastfood.integration.service;

import com.fastfood.dto.request.OrderRequest;
import com.fastfood.dto.request.PaymentRequest;
import com.fastfood.entity.catalog.Food;
import com.fastfood.entity.catalog.FoodIngredient;
import com.fastfood.entity.catalog.Ingredient;
import com.fastfood.entity.system.Role;
import com.fastfood.entity.system.User;
import com.fastfood.entity.transaction.Order;
import com.fastfood.repository.*;
import com.fastfood.service.impl.SalesServiceImpl;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class SalesIntegrationTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private SalesServiceImpl salesService;


    @Autowired
    private UserRepository userRepository;


    @Autowired
    private FoodRepository foodRepository;


    @Autowired
    private IngredientRepository ingredientRepository;


    @Autowired
    private OrderRepository orderRepository;


    @Autowired
    private SalesInvoiceRepository salesInvoiceRepository;

    private Ingredient ingredient;

    private Food food;

    @BeforeEach
    void setup(){

        Role role = entityManager.find(Role.class, "R001");

        User tableUser = userRepository.findByUsername("ban01");

        if(tableUser == null){

            tableUser = new User();

            tableUser.setRole(role);
            tableUser.setIdUser("U001");
            tableUser.setUsername("ban01");
            tableUser.setPasswordHash("123");
            tableUser.setFullName("Ban01");

            userRepository.save(tableUser);
        }


        ingredient = Ingredient.builder()
                .idIngredient("NL001")
                .ingredientName("Thit")
                .unit("kg")
                .quantityStock(new BigDecimal("10"))
                .importPrice(new BigDecimal("50000"))
                .build();

        ingredientRepository.save(ingredient);


        food = new Food();

        food.setIdFood("H001");
        food.setFoodName("Ga Ran");
        food.setUnitPrice(new BigDecimal("30000"));


        FoodIngredient foodIngredient =
                new FoodIngredient();

        foodIngredient.setFood(food);
        foodIngredient.setIngredient(ingredient);
        foodIngredient.setQuantityUsed(new BigDecimal("1"));


        food.setFoodIngredients(
                List.of(foodIngredient)
        );

        foodRepository.save(food);
    }

    @Test
    void placeOrder_success_shouldCreateOrderAndDecreaseStock(){

        OrderRequest request =
                new OrderRequest();

        request.setTableNumber("Ban01");
        request.setCustomerName("Customer");
        request.setCreatedBy("U001");

        OrderRequest.OrderItemDto item =
                new OrderRequest.OrderItemDto();

        item.setFoodId("H001");
        item.setQuantity(1);

        request.setItems(
                List.of(item)
        );
        Order result =
                salesService.placeOrder(request);

        assertNotNull(result);
        assertEquals(
                "PENDING",
                result.getStatus()
        );

        assertEquals(
                "N01",
                result.getTableNumber()
        );

        Ingredient updated =
                ingredientRepository
                        .findById("NL001")
                        .get();

        assertEquals(
                new BigDecimal("9"),
                updated.getQuantityStock()
        );

        assertEquals(
                3,
                result.getOrderDetails().size()
        );

    }

    @Test
    void processPayment_success_shouldCreateInvoiceAndChangeStatus(){

        Order order =
                createOrder();

        PaymentRequest request =
                new PaymentRequest();

        request.setOrderId(
                order.getIdOrder()
        );

        request.setCustomerPhone(
                "0900000000"
        );

        request.setPaymentMethod(
                "CASH"
        );

        var response =
                salesService.processPayment(request);

        assertNotNull(response);

        Order updated =
                orderRepository
                        .findById(order.getIdOrder())
                        .get();

        assertEquals(
                "PAID",
                updated.getStatus()
        );

        assertTrue(
                salesInvoiceRepository
                        .findById(response.getInvoiceId())
                        .isPresent()
        );

    }

    private Order createOrder(){

        Order order = new Order();

        order.setIdOrder("ORD001");
        order.setTableNumber("N01");
        order.setCustomerName("Test");
        order.setStatus("PENDING");
        order.setCreatedBy("U001");

        var detail =
                new com.fastfood.entity.transaction.OrderDetail();

        detail.setOrder(order);
        detail.setFood(food);
        detail.setQuantity(2);
        detail.setUnitPrice(
                food.getUnitPrice()
        );

        order.setOrderDetails(
                List.of(detail)
        );

        return orderRepository.save(order);

    }

}