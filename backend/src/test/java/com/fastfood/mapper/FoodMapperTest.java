package com.fastfood.mapper;

import com.fastfood.dto.request.FoodIngredientRequest;
import com.fastfood.dto.request.FoodRequest;
import com.fastfood.dto.response.FoodKitchenResponse;
import com.fastfood.dto.response.FoodMenuResponse;
import com.fastfood.entity.catalog.*;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FoodMapperTest {

    private final FoodMapper mapper =
            Mappers.getMapper(FoodMapper.class);

    @Test
    void toFoodEntity() {

        FoodRequest request = new FoodRequest();
        request.setFoodName("Burger");
        request.setIdCategory("CAT01");
        request.setUnitPrice(BigDecimal.valueOf(50000));

        Food entity = mapper.toFoodEntity(request);

        assertNull(entity.getIdFood());
        assertEquals("Burger", entity.getFoodName());
        assertEquals("CAT01",
                entity.getFoodCategory().getIdCategory());
    }

    @Test
    void toFoodIngredientEntity() {

        FoodIngredientRequest request =
                new FoodIngredientRequest();

        request.setIdIngredient("NL01");

        FoodIngredient entity =
                mapper.toFoodIngredientEntity(request);

        assertEquals("NL01",
                entity.getIngredient().getIdIngredient());

        assertNull(entity.getFood());
    }

    @Test
    void toFoodMenuResponse() {

        FoodCategory category = new FoodCategory();
        category.setIdCategory("CAT01");

        Food food = new Food();
        food.setIdFood("H01");
        food.setFoodName("Pizza");
        food.setFoodCategory(category);

        FoodMenuResponse response =
                mapper.toFoodMenuResponse(food);

        assertEquals("CAT01", response.getIdCategory());
        assertEquals("Pizza", response.getFoodName());
    }

    @Test
    void toFoodKitchenResponse() {

        Ingredient ingredient = new Ingredient();
        ingredient.setIdIngredient("NL01");
        ingredient.setIngredientName("Thịt");
        ingredient.setUnit("Kg");

        FoodIngredient fi = new FoodIngredient();
        fi.setIngredient(ingredient);

        Food food = new Food();
        food.setFoodName("Pizza");
        food.setFoodIngredients(List.of(fi));

        FoodKitchenResponse response =
                mapper.toFoodKitchenResponse(food);

        assertEquals(1, response.getIngredients().size());
        assertEquals("NL01",
                response.getIngredients().get(0).getIdIngredient());
    }

    @Test
    void updateFood() {

        FoodRequest request = new FoodRequest();
        request.setFoodName("Burger mới");
        request.setIdCategory("CAT02");

        Food food = new Food();
        food.setIdFood("H01");
        food.setFoodName("Burger cũ");
        food.setFoodIngredients(List.of(new FoodIngredient()));

        mapper.updateFoodFromRequest(request, food);

        assertEquals("H01", food.getIdFood());
        assertEquals("Burger mới", food.getFoodName());
        assertEquals("CAT02",
                food.getFoodCategory().getIdCategory());

        assertEquals(1,
                food.getFoodIngredients().size());
    }
}