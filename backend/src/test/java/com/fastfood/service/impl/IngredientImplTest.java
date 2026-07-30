package com.fastfood.service.impl;

import com.fastfood.dto.request.IngredientRequest;
import com.fastfood.dto.response.IngredientResponse;
import com.fastfood.entity.catalog.Ingredient;
import com.fastfood.mapper.IngredientMapper;
import com.fastfood.repository.IngredientRepository;
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
class IngredientImplTest {

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private IngredientMapper ingredientMapper;

    @InjectMocks
    private IngredientImpl service;

    @Test
    void getAllIngredient_Success() {

        Ingredient ingredient = new Ingredient();
        IngredientResponse response = new IngredientResponse();

        when(ingredientRepository.findAll())
                .thenReturn(List.of(ingredient));

        when(ingredientMapper.toIngredientResponse(ingredient))
                .thenReturn(response);

        List<IngredientResponse> result = service.getAllIngredient();

        assertEquals(1, result.size());

        verify(ingredientRepository).findAll();
        verify(ingredientMapper).toIngredientResponse(ingredient);
    }

    @Test
    void getAllIngredient_Empty() {

        when(ingredientRepository.findAll())
                .thenReturn(List.of());

        List<IngredientResponse> result = service.getAllIngredient();

        assertTrue(result.isEmpty());
    }

    @Test
    void getIngredientById_Success() {

        Ingredient ingredient = new Ingredient();
        IngredientResponse response = new IngredientResponse();

        when(ingredientRepository.findById("NL001"))
                .thenReturn(Optional.of(ingredient));

        when(ingredientMapper.toIngredientResponse(ingredient))
                .thenReturn(response);

        IngredientResponse result = service.getIngredientById("NL001");

        assertNotNull(result);
    }

    @Test
    void getIngredientById_NotFound() {

        when(ingredientRepository.findById("NL999"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.getIngredientById("NL999"));
    }

    @Test
    void generateNextIdIngredient_FirstId() {

        when(ingredientRepository.findMaxidIngredient())
                .thenReturn(null);

        String id = service.generateNextIdIngredient();

        assertEquals("NL001", id);
    }

    @Test
    void generateNextIdIngredient_NextId() {

        when(ingredientRepository.findMaxidIngredient())
                .thenReturn("NL009");

        String id = service.generateNextIdIngredient();

        assertEquals("NL010", id);
    }

    @Test
    void saveIngredient_Success() {

        IngredientRequest request = new IngredientRequest();

        Ingredient entity = new Ingredient();

        IngredientResponse response = new IngredientResponse();

        when(ingredientMapper.toIngredientEntity(request))
                .thenReturn(entity);

        when(ingredientRepository.findMaxidIngredient())
                .thenReturn("NL001");

        when(ingredientRepository.save(entity))
                .thenReturn(entity);

        when(ingredientMapper.toIngredientResponse(entity))
                .thenReturn(response);

        IngredientResponse result = service.saveIngredient(request);

        assertNotNull(result);

        assertEquals("NL002", entity.getIdIngredient());

        verify(ingredientRepository).save(entity);
    }

    @Test
    void updateIngredient_Success() {

        IngredientRequest request = new IngredientRequest();

        Ingredient ingredient = new Ingredient();

        IngredientResponse response = new IngredientResponse();

        when(ingredientRepository.findById("NL001"))
                .thenReturn(Optional.of(ingredient));

        when(ingredientRepository.save(ingredient))
                .thenReturn(ingredient);

        when(ingredientMapper.toIngredientResponse(ingredient))
                .thenReturn(response);

        IngredientResponse result =
                service.updateIngredient(request, "NL001");

        assertNotNull(result);

        verify(ingredientMapper)
                .updateIngredienFromRequest(request, ingredient);

        verify(ingredientRepository).save(ingredient);
    }

    @Test
    void updateIngredient_NotFound() {

        when(ingredientRepository.findById("NL999"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.updateIngredient(
                        new IngredientRequest(),
                        "NL999"));
    }

    @Test
    void deleteIngredient_Success() {

        service.deleteIngredient("NL001");

        verify(ingredientRepository)
                .deleteById("NL001");
    }

    @Test
    void deleteIngredient_DataIntegrityViolation() {

        doThrow(new DataIntegrityViolationException(""))
                .when(ingredientRepository)
                .deleteById("NL001");

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> service.deleteIngredient("NL001"));

        assertTrue(ex.getMessage().contains("Không thể xóa"));
    }

}