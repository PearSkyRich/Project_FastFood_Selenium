package com.fastfood.mapper;

import com.fastfood.dto.request.FoodCategoryRequest;
import com.fastfood.dto.response.FoodCategoryResponse;
import com.fastfood.entity.catalog.FoodCategory;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class FoodCategoryMapperTest {

    private final FoodCategoryMapper mapper =
            Mappers.getMapper(FoodCategoryMapper.class);

    @Test
    void toResponse() {

        FoodCategory entity = new FoodCategory();
        entity.setIdCategory("CAT01");
        entity.setCategoryName("Đồ ăn");

        FoodCategoryResponse response = mapper.toResponse(entity);

        assertEquals("CAT01", response.getIdCategory());
        assertEquals("Đồ ăn", response.getCategoryName());
    }

    @Test
    void toEntity() {

        FoodCategoryRequest request = new FoodCategoryRequest();
        request.setCategoryName("Nước uống");

        FoodCategory entity = mapper.toEntity(request);

        assertNull(entity.getIdCategory());
        assertEquals("Nước uống", entity.getCategoryName());
    }

    @Test
    void updateEntity() {

        FoodCategoryRequest request = new FoodCategoryRequest();
        request.setCategoryName("Món mới");

        FoodCategory entity = new FoodCategory();
        entity.setIdCategory("CAT01");
        entity.setCategoryName("Cũ");

        mapper.updateEntity(request, entity);

        assertEquals("CAT01", entity.getIdCategory());
        assertEquals("Món mới", entity.getCategoryName());
    }
}