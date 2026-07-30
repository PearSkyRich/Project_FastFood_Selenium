package com.fastfood.mapper;

import com.fastfood.dto.request.IngredientRequest;
import com.fastfood.dto.response.IngredientResponse;
import com.fastfood.entity.catalog.Ingredient;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class IngredientMapperTest {

    private final IngredientMapper mapper =
            Mappers.getMapper(IngredientMapper.class);

    @Test
    void toResponse() {

        Ingredient ingredient = new Ingredient();
        ingredient.setIdIngredient("NL01");
        ingredient.setIngredientName("Thịt");
        ingredient.setUnit("Kg");
        ingredient.setQuantityStock(BigDecimal.TEN);

        IngredientResponse response =
                mapper.toIngredientResponse(ingredient);

        assertEquals("NL01", response.getIdIngredient());
        assertEquals("Thịt", response.getIngredientName());
    }

    @Test
    void toEntity() {

        IngredientRequest request = new IngredientRequest();
        request.setIngredientName("Đường");
        request.setUnit("Kg");

        Ingredient ingredient =
                mapper.toIngredientEntity(request);

        assertEquals("Đường", ingredient.getIngredientName());
        assertEquals("Kg", ingredient.getUnit());
    }

    @Test
    void updateIngredient() {

        IngredientRequest request = new IngredientRequest();
        request.setIngredientName("Muối");
        request.setUnit("Gói");

        Ingredient ingredient = new Ingredient();
        ingredient.setIdIngredient("NL01");
        ingredient.setIngredientName("Cũ");

        mapper.updateIngredienFromRequest(request, ingredient);

        assertEquals("NL01", ingredient.getIdIngredient());
        assertEquals("Muối", ingredient.getIngredientName());
        assertEquals("Gói", ingredient.getUnit());
    }
}