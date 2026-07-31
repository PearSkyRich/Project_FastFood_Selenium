package com.fastfood.integration.service;


import com.fastfood.entity.catalog.Food;
import com.fastfood.repository.FoodRepository;
import com.fastfood.service.IFoodService;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


import java.math.BigDecimal;


import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
class FoodIntegrationTest {


    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private IFoodService foodService;

    @Transactional
    @Test
    void createFood_thenGetFood(){

        Food food = new Food();

        food.setIdFood("H001");
        food.setFoodName("Burger");

        food.setUnitPrice(
                BigDecimal.valueOf(50000)
        );

        foodRepository.save(food);

        var result =
                foodService.getMenuById("H001");

        assertNotNull(result);

        assertEquals(
                "Burger",
                result.getFoodName()
        );

    }
}