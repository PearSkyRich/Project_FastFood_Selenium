package com.fastfood.service.impl;

import com.fastfood.dto.request.FoodIngredientRequest;
import com.fastfood.dto.request.FoodRequest;
import com.fastfood.dto.response.FoodKitchenResponse;
import com.fastfood.dto.response.FoodMenuResponse;
import com.fastfood.entity.catalog.Food;
import com.fastfood.entity.catalog.FoodIngredient;
import com.fastfood.entity.catalog.Ingredient;
import com.fastfood.mapper.FoodMapper;
import com.fastfood.repository.FoodRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FoodImplTest {

    @Mock
    private FoodRepository foodRepository;

    @Mock
    private FoodMapper foodMapper;

    @InjectMocks
    private FoodImpl service;

    //======================
    // Helper
    //======================

    private Food createFood(BigDecimal stock, BigDecimal used) {

        Ingredient ingredient = new Ingredient();
        ingredient.setIdIngredient("NL001");
        ingredient.setIngredientName("Gà");
        ingredient.setUnit("kg");
        ingredient.setImportPrice(BigDecimal.valueOf(100));
        ingredient.setQuantityStock(stock);

        FoodIngredient item = new FoodIngredient();
        item.setIngredient(ingredient);
        item.setQuantityUsed(used);

        Food food = new Food();
        food.setIdFood("H001");
        food.setFoodName("Burger");
        food.setUnitPrice(BigDecimal.valueOf(50000));

        List<FoodIngredient> list = new ArrayList<>();
        list.add(item);

        food.setFoodIngredients(list);

        return food;
    }

    //====================================================
    // getAllMenu
    //====================================================

    @Test
    void getAllMenu_Available() {

        Food food = createFood(
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(2));

        FoodMenuResponse response = new FoodMenuResponse();

        when(foodRepository.findAll())
                .thenReturn(List.of(food));

        when(foodMapper.toFoodMenuResponse(food))
                .thenReturn(response);

        List<FoodMenuResponse> result =
                service.getAllMenu();

        assertEquals(1, result.size());

        assertTrue(result.get(0).isAvailable());

        verify(foodRepository).findAll();
    }

    @Test
    void getAllMenu_NotAvailable() {

        Food food = createFood(
                BigDecimal.ONE,
                BigDecimal.valueOf(5));

        FoodMenuResponse response = new FoodMenuResponse();

        when(foodRepository.findAll())
                .thenReturn(List.of(food));

        when(foodMapper.toFoodMenuResponse(food))
                .thenReturn(response);

        List<FoodMenuResponse> result =
                service.getAllMenu();

        assertFalse(result.get(0).isAvailable());
    }

    @Test
    void getAllMenu_Empty() {

        when(foodRepository.findAll())
                .thenReturn(List.of());

        List<FoodMenuResponse> result =
                service.getAllMenu();

        assertTrue(result.isEmpty());
    }

    //====================================================
    // getMenuById
    //====================================================

    @Test
    void getMenuById_Success() {

        Food food = createFood(
                BigDecimal.TEN,
                BigDecimal.ONE);

        FoodMenuResponse response =
                new FoodMenuResponse();

        when(foodRepository.findById("H001"))
                .thenReturn(Optional.of(food));

        when(foodMapper.toFoodMenuResponse(food))
                .thenReturn(response);

        FoodMenuResponse result =
                service.getMenuById("H001");

        assertNotNull(result);

        assertTrue(result.isAvailable());

        verify(foodRepository).findById("H001");
    }

    @Test
    void getMenuById_NotFound() {

        when(foodRepository.findById("H999"))
                .thenReturn(Optional.empty());

        RuntimeException ex =
                assertThrows(RuntimeException.class,
                        () -> service.getMenuById("H999"));

        assertTrue(ex.getMessage()
                .contains("Không tìm thấy"));
    }

    //====================================================
    // getFoodForKitchen
    //====================================================

    @Test
    void getFoodForKitchen_Success() {

        Food food = new Food();

        FoodKitchenResponse response =
                new FoodKitchenResponse();

        when(foodRepository.findById("H001"))
                .thenReturn(Optional.of(food));

        when(foodMapper.toFoodKitchenResponse(food))
                .thenReturn(response);

        FoodKitchenResponse result =
                service.getFoodForKitchen("H001");

        assertNotNull(result);
    }

    @Test
    void getFoodForKitchen_NotFound() {

        when(foodRepository.findById("H999"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.getFoodForKitchen("H999"));
    }
    //====================================================
    // createFood
    //====================================================

    @Test
    void createFood_Success() {

        FoodRequest request = new FoodRequest();

        Food entity = new Food();
        entity.setFoodIngredients(new ArrayList<>());

        FoodKitchenResponse response = new FoodKitchenResponse();

        when(foodMapper.toFoodEntity(request))
                .thenReturn(entity);

        when(foodRepository.findMaxidFood())
                .thenReturn("H009");

        when(foodRepository.save(entity))
                .thenReturn(entity);

        when(foodMapper.toFoodKitchenResponse(entity))
                .thenReturn(response);

        FoodKitchenResponse result =
                service.createFood(request);

        assertNotNull(result);

        assertEquals("H010", entity.getIdFood());

        verify(foodRepository).save(entity);
    }

    @Test
    void createFood_WithIngredients() {

        FoodRequest request = new FoodRequest();

        Food food = new Food();

        FoodIngredient item = new FoodIngredient();

        List<FoodIngredient> ingredients = new ArrayList<>();
        ingredients.add(item);

        food.setFoodIngredients(ingredients);

        when(foodMapper.toFoodEntity(request))
                .thenReturn(food);

        when(foodRepository.findMaxidFood())
                .thenReturn("H001");

        when(foodRepository.save(food))
                .thenReturn(food);

        when(foodMapper.toFoodKitchenResponse(food))
                .thenReturn(new FoodKitchenResponse());

        service.createFood(request);

        assertEquals(food, item.getFood());

        verify(foodRepository).save(food);
    }

    //====================================================
    // updateFood
    //====================================================

    @Test
    void updateFood_Success() {

        FoodRequest request = new FoodRequest();

        Food food = new Food();
        food.setFoodIngredients(new ArrayList<>());

        when(foodRepository.findById("H001"))
                .thenReturn(Optional.of(food));

        when(foodRepository.save(food))
                .thenReturn(food);

        when(foodMapper.toFoodKitchenResponse(food))
                .thenReturn(new FoodKitchenResponse());

        FoodKitchenResponse result =
                service.updateFood("H001", request);

        assertNotNull(result);

        verify(foodMapper)
                .updateFoodFromRequest(request, food);

        verify(foodRepository)
                .save(food);
    }

    @Test
    void updateFood_NotFound() {

        when(foodRepository.findById("H999"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.updateFood(
                        "H999",
                        new FoodRequest()));
    }

    @Test
    void updateFood_RequestWithoutIngredients() {

        FoodRequest request = new FoodRequest();
        request.setIngredients(null);

        Food food = new Food();
        food.setFoodIngredients(new ArrayList<>());

        when(foodRepository.findById("H001"))
                .thenReturn(Optional.of(food));

        when(foodRepository.save(food))
                .thenReturn(food);

        when(foodMapper.toFoodKitchenResponse(food))
                .thenReturn(new FoodKitchenResponse());

        service.updateFood("H001", request);

        assertTrue(food.getFoodIngredients().isEmpty());

        verify(foodRepository).save(food);
    }

    //====================================================
    // deleteFood
    //====================================================

    @Test
    void deleteFood_Success() {

        service.deleteFood("H001");

        verify(foodRepository)
                .deleteById("H001");
    }

    @Test
    void deleteFood_VerifyOnlyOneCall() {

        service.deleteFood("H001");

        verify(foodRepository, times(1))
                .deleteById("H001");

        verifyNoMoreInteractions(foodRepository);
    }
    //====================================================
    // getFoodCosts
    //====================================================

    @Test
    void getFoodCosts_Success() {

        Ingredient ingredient = new Ingredient();
        ingredient.setIdIngredient("NL001");
        ingredient.setIngredientName("Thịt bò");
        ingredient.setUnit("kg");
        ingredient.setImportPrice(BigDecimal.valueOf(100));

        FoodIngredient item = new FoodIngredient();
        item.setIngredient(ingredient);
        item.setQuantityUsed(BigDecimal.valueOf(2));

        Food food = new Food();
        food.setIdFood("H001");
        food.setFoodName("Burger");
        food.setUnitPrice(BigDecimal.valueOf(500));
        food.setFoodIngredients(List.of(item));

        when(foodRepository.findAllWithIngredients())
                .thenReturn(List.of(food));

        var result = service.getFoodCosts();

        assertEquals(1, result.size());

        assertEquals(
                BigDecimal.valueOf(200),
                result.get(0).getProductionCost());

        assertEquals(
                BigDecimal.valueOf(300),
                result.get(0).getGrossProfit());
    }

    @Test
    void getFoodCosts_NoIngredient() {

        Food food = new Food();
        food.setIdFood("H001");
        food.setFoodName("Burger");
        food.setUnitPrice(BigDecimal.valueOf(100));
        food.setFoodIngredients(new ArrayList<>());

        when(foodRepository.findAllWithIngredients())
                .thenReturn(List.of(food));

        var result = service.getFoodCosts();

        assertEquals(1, result.size());

        assertEquals(
                BigDecimal.ZERO,
                result.get(0).getProductionCost());

        assertEquals(
                BigDecimal.valueOf(100),
                result.get(0).getGrossProfit());
    }

    @Test
    void getFoodCosts_NullIngredientList() {

        Food food = new Food();
        food.setIdFood("H001");
        food.setFoodName("Burger");
        food.setUnitPrice(BigDecimal.valueOf(100));
        food.setFoodIngredients(null);

        when(foodRepository.findAllWithIngredients())
                .thenReturn(List.of(food));

        var result = service.getFoodCosts();

        assertEquals(1, result.size());

        assertEquals(
                BigDecimal.ZERO,
                result.get(0).getProductionCost());
    }

    @Test
    void getFoodCosts_NullPrice() {

        Ingredient ingredient = new Ingredient();
        ingredient.setImportPrice(null);

        FoodIngredient item = new FoodIngredient();
        item.setIngredient(ingredient);
        item.setQuantityUsed(BigDecimal.TEN);

        Food food = new Food();
        food.setUnitPrice(null);
        food.setFoodIngredients(List.of(item));

        when(foodRepository.findAllWithIngredients())
                .thenReturn(List.of(food));

        var result = service.getFoodCosts();

        assertEquals(
                BigDecimal.ZERO,
                result.get(0).getSalePrice());

        assertEquals(
                BigDecimal.ZERO,
                result.get(0).getProductionCost());

        assertEquals(
                BigDecimal.ZERO,
                result.get(0).getGrossProfit());
    }

    @Test
    void getFoodCosts_MarginPercent() {

        Ingredient ingredient = new Ingredient();
        ingredient.setImportPrice(BigDecimal.valueOf(25));

        FoodIngredient item = new FoodIngredient();
        item.setIngredient(ingredient);
        item.setQuantityUsed(BigDecimal.valueOf(2));

        Food food = new Food();
        food.setUnitPrice(BigDecimal.valueOf(100));
        food.setFoodIngredients(List.of(item));

        when(foodRepository.findAllWithIngredients())
                .thenReturn(List.of(food));

        var result = service.getFoodCosts();

        assertEquals(
                BigDecimal.valueOf(50),
                result.get(0).getProductionCost());

        assertEquals(
                BigDecimal.valueOf(50),
                result.get(0).getGrossProfit());

        assertEquals(
                0,
                result.get(0).getMarginPercent()
                        .compareTo(BigDecimal.valueOf(50))
        );
    }

    @Test
    void getFoodCosts_EmptyList() {

        when(foodRepository.findAllWithIngredients())
                .thenReturn(List.of());

        var result = service.getFoodCosts();

        assertTrue(result.isEmpty());
    }

    @Test
    void getFoodCosts_VerifyRepositoryCalled() {

        when(foodRepository.findAllWithIngredients())
                .thenReturn(List.of());

        service.getFoodCosts();

        verify(foodRepository, times(1))
                .findAllWithIngredients();
    }
    @Test
    void updateFood_WithIngredients() {

        FoodIngredientRequest ingredientRequest = new FoodIngredientRequest();
        ingredientRequest.setIdIngredient("NL001");
        ingredientRequest.setQuantityUsed(BigDecimal.valueOf(2));

        FoodRequest request = new FoodRequest();
        request.setIngredients(List.of(ingredientRequest));

        Food existing = new Food();
        existing.setFoodIngredients(new ArrayList<>());

        FoodIngredient ingredient = new FoodIngredient();

        when(foodRepository.findById("H001"))
                .thenReturn(Optional.of(existing));

        when(foodMapper.toFoodIngredientEntity(ingredientRequest))
                .thenReturn(ingredient);

        when(foodRepository.save(existing))
                .thenReturn(existing);

        when(foodMapper.toFoodKitchenResponse(existing))
                .thenReturn(new FoodKitchenResponse());

        service.updateFood("H001", request);

        assertEquals(1, existing.getFoodIngredients().size());

        assertEquals(existing, ingredient.getFood());

        verify(foodMapper).toFoodIngredientEntity(ingredientRequest);
    }
    @Test
    void getAllMenu_StockNull() {

        Ingredient ingredient = new Ingredient();
        ingredient.setQuantityStock(null);

        FoodIngredient item = new FoodIngredient();
        item.setIngredient(ingredient);
        item.setQuantityUsed(BigDecimal.ONE);

        Food food = new Food();
        food.setFoodIngredients(List.of(item));

        FoodMenuResponse response = new FoodMenuResponse();

        when(foodRepository.findAll()).thenReturn(List.of(food));
        when(foodMapper.toFoodMenuResponse(food)).thenReturn(response);

        List<FoodMenuResponse> result = service.getAllMenu();

        assertFalse(result.get(0).isAvailable());
    }
    @Test
    void getAllMenu_QuantityUsedNull() {

        Ingredient ingredient = new Ingredient();
        ingredient.setQuantityStock(BigDecimal.ONE);

        FoodIngredient item = new FoodIngredient();
        item.setIngredient(ingredient);
        item.setQuantityUsed(null);

        Food food = new Food();
        food.setFoodIngredients(List.of(item));

        FoodMenuResponse response = new FoodMenuResponse();

        when(foodRepository.findAll()).thenReturn(List.of(food));
        when(foodMapper.toFoodMenuResponse(food)).thenReturn(response);

        List<FoodMenuResponse> result = service.getAllMenu();

        assertTrue(result.get(0).isAvailable());
    }
    @Test
    void getAllMenu_NoIngredient() {

        Food food = new Food();
        food.setFoodIngredients(new ArrayList<>());

        FoodMenuResponse response = new FoodMenuResponse();

        when(foodRepository.findAll()).thenReturn(List.of(food));
        when(foodMapper.toFoodMenuResponse(food)).thenReturn(response);

        List<FoodMenuResponse> result = service.getAllMenu();

        assertTrue(result.get(0).isAvailable());
    }
    @Test
    void getFoodCosts_IngredientNull() {

        FoodIngredient item = new FoodIngredient();
        item.setIngredient(null);
        item.setQuantityUsed(BigDecimal.TEN);

        Food food = new Food();
        food.setUnitPrice(BigDecimal.valueOf(100));
        food.setFoodIngredients(List.of(item));

        when(foodRepository.findAllWithIngredients())
                .thenReturn(List.of(food));

        var result = service.getFoodCosts();

        assertEquals(BigDecimal.ZERO,
                result.get(0).getProductionCost());
    }
    @Test
    void getFoodCosts_QuantityUsedNull() {

        Ingredient ingredient = new Ingredient();
        ingredient.setImportPrice(BigDecimal.valueOf(100));

        FoodIngredient item = new FoodIngredient();
        item.setIngredient(ingredient);
        item.setQuantityUsed(null);

        Food food = new Food();
        food.setUnitPrice(BigDecimal.valueOf(100));
        food.setFoodIngredients(List.of(item));

        when(foodRepository.findAllWithIngredients())
                .thenReturn(List.of(food));

        var result = service.getFoodCosts();

        assertEquals(BigDecimal.ZERO,
                result.get(0).getProductionCost());
    }
    @Test
    void getFoodCosts_ImportPriceNull() {

        Ingredient ingredient = new Ingredient();
        ingredient.setImportPrice(null);

        FoodIngredient item = new FoodIngredient();
        item.setIngredient(ingredient);
        item.setQuantityUsed(BigDecimal.TEN);

        Food food = new Food();
        food.setUnitPrice(BigDecimal.valueOf(500));
        food.setFoodIngredients(List.of(item));

        when(foodRepository.findAllWithIngredients())
                .thenReturn(List.of(food));

        var result = service.getFoodCosts();

        assertEquals(BigDecimal.ZERO,
                result.get(0).getProductionCost());
    }
    @Test
    void createFood_SetParentReference() {

        FoodRequest request = new FoodRequest();

        FoodIngredient item = new FoodIngredient();

        Food food = new Food();
        food.setFoodIngredients(new ArrayList<>(List.of(item)));

        when(foodMapper.toFoodEntity(request))
                .thenReturn(food);

        when(foodRepository.findMaxidFood())
                .thenReturn("H001");

        when(foodRepository.save(food))
                .thenReturn(food);

        when(foodMapper.toFoodKitchenResponse(food))
                .thenReturn(new FoodKitchenResponse());

        service.createFood(request);

        assertSame(food, item.getFood());
    }
}