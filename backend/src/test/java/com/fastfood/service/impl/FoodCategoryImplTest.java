package com.fastfood.service.impl;

import com.fastfood.dto.request.FoodCategoryRequest;
import com.fastfood.dto.response.FoodCategoryResponse;
import com.fastfood.entity.catalog.FoodCategory;
import com.fastfood.mapper.FoodCategoryMapper;
import com.fastfood.repository.FoodCategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FoodCategoryImplTest {

    @Mock
    private FoodCategoryRepository repository;

    @Mock
    private FoodCategoryMapper mapper;

    @InjectMocks
    private FoodCategoryImpl service;

    @Test
    void getAllCategory_Success() {

        FoodCategory entity = new FoodCategory();
        FoodCategoryResponse response = new FoodCategoryResponse();

        when(repository.findAll()).thenReturn(List.of(entity));
        when(mapper.toResponse(entity)).thenReturn(response);

        List<FoodCategoryResponse> result = service.getAllCategory();

        assertEquals(1, result.size());

        verify(repository).findAll();
        verify(mapper).toResponse(entity);
    }

    @Test
    void getCategoryById_Success() {

        FoodCategory entity = new FoodCategory();
        FoodCategoryResponse response = new FoodCategoryResponse();

        when(repository.findById("LH001"))
                .thenReturn(Optional.of(entity));

        when(mapper.toResponse(entity))
                .thenReturn(response);

        FoodCategoryResponse result = service.getCategoryById("LH001");

        assertNotNull(result);
    }

    @Test
    void getCategoryById_NotFound() {

        when(repository.findById("LH999"))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> service.getCategoryById("LH999")
        );

        assertTrue(ex.getMessage().contains("Không tìm thấy"));
    }

    @Test
    void saveCategory_Success() {

        FoodCategoryRequest request = new FoodCategoryRequest();

        FoodCategory entity = new FoodCategory();

        FoodCategory saved = new FoodCategory();

        FoodCategoryResponse response = new FoodCategoryResponse();

        when(mapper.toEntity(request)).thenReturn(entity);

        when(repository.findMaxIdCategory()).thenReturn("LH009");

        when(repository.save(entity)).thenReturn(saved);

        when(mapper.toResponse(saved)).thenReturn(response);

        FoodCategoryResponse result = service.saveCategory(request);

        assertNotNull(result);

        assertEquals("LH010", entity.getIdCategory());

        verify(repository).save(entity);
    }

    @Test
    void updateCategory_Success() {

        FoodCategoryRequest request = new FoodCategoryRequest();

        FoodCategory entity = new FoodCategory();

        FoodCategoryResponse response = new FoodCategoryResponse();

        when(repository.findById("LH001"))
                .thenReturn(Optional.of(entity));

        when(repository.save(entity))
                .thenReturn(entity);

        when(mapper.toResponse(entity))
                .thenReturn(response);

        FoodCategoryResponse result =
                service.updateCategory("LH001", request);

        assertNotNull(result);

        verify(mapper).updateEntity(request, entity);

        verify(repository).save(entity);
    }

    @Test
    void updateCategory_NotFound() {

        when(repository.findById("LH999"))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> service.updateCategory("LH999", new FoodCategoryRequest())
        );
    }

    @Test
    void deleteCategory_Success() {

        FoodCategory entity = new FoodCategory();

        when(repository.findById("LH001"))
                .thenReturn(Optional.of(entity));

        service.deleteCategory("LH001");

        verify(repository).delete(entity);
    }

    @Test
    void deleteCategory_InUse() {

        FoodCategory entity = new FoodCategory();

        when(repository.findById("LH001"))
                .thenReturn(Optional.of(entity));

        doThrow(new DataIntegrityViolationException(""))
                .when(repository)
                .delete(entity);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> service.deleteCategory("LH001")
        );

        assertTrue(ex.getMessage().contains("đang được sử dụng"));
    }

    @Test
    void generateNextIdCategory_FirstId() {

        when(repository.findMaxIdCategory())
                .thenReturn(null);

        String id = service.generateNextIdCategory();

        assertEquals("LH001", id);
    }

    @Test
    void generateNextIdCategory_NextId() {

        when(repository.findMaxIdCategory())
                .thenReturn("LH099");

        String id = service.generateNextIdCategory();

        assertEquals("LH100", id);
    }

}