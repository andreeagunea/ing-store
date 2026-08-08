package com.ing.andreea.teastore.service;

import com.ing.andreea.teastore.dto.PriceUpdate;
import com.ing.andreea.teastore.dto.Tea;
import com.ing.andreea.teastore.dto.TeaRequest;
import com.ing.andreea.teastore.exception.DuplicateTeaException;
import com.ing.andreea.teastore.exception.TeaNotFoundException;
import com.ing.andreea.teastore.model.entity.TeaEntity;
import com.ing.andreea.teastore.model.enums.TeaCategory;
import com.ing.andreea.teastore.repository.TeaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeaServiceTest {

    @Mock
    private TeaRepository teaRepository;

    @InjectMocks
    private TeaService teaService;

    private TeaEntity teaEntity;
    private TeaRequest teaRequest;

    @BeforeEach
    void setUp() {
        teaEntity = TeaEntity.builder()
                .id(1L)
                .name("Green Tea")
                .description("A fresh green tea")
                .category(TeaCategory.GREEN)
                .price(new BigDecimal("5.99"))
                .stockQuantity(100)
                .origin("China")
                .build();

        teaRequest = new TeaRequest();
        teaRequest.setName("Green Tea");
        teaRequest.setDescription("A fresh green tea");
        teaRequest.setCategory(TeaCategory.GREEN);
        teaRequest.setPrice(new BigDecimal("5.99"));
        teaRequest.setStockQuantity(100);
        teaRequest.setOrigin("China");
    }

    @Test
    void returnAllTeasSuccessfully() {
        when(teaRepository.findAll()).thenReturn(List.of(teaEntity));
        List<Tea> result = teaService.getAllTeas();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Green Tea", result.getFirst().getName());
        assertEquals(TeaCategory.GREEN, result.getFirst().getCategory());
        verify(teaRepository, times(1)).findAll();
    }

    @Test
    void returnTeasByNameSuccessfully() {
        when(teaRepository.findByNameIgnoreCase("Green Tea")).thenReturn(Optional.ofNullable(teaEntity));
        List<Tea> result = teaService.getTeasByName("Green Tea");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Green Tea", result.getFirst().getName());
        verify(teaRepository, times(1)).findByNameIgnoreCase("Green Tea");
    }

    @Test
    void returnTeasByCategorySuccessfully() {
        when(teaRepository.findByCategory(TeaCategory.GREEN)).thenReturn(List.of(teaEntity));

        List<Tea> result = teaService.getTeasByCategory(TeaCategory.GREEN);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(TeaCategory.GREEN, result.getFirst().getCategory());
        verify(teaRepository, times(1)).findByCategory(TeaCategory.GREEN);
    }

    @Test
    void returnTeasByPriceBetweenSuccessfully() {
        BigDecimal min = new BigDecimal("1.00");
        BigDecimal max = new BigDecimal("10.00");

        when(teaRepository.findByPriceBetween(min, max)).thenReturn(List.of(teaEntity));

        List<Tea> result = teaService.getTeasByPriceBetween(min, max);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(new BigDecimal("5.99"), result.getFirst().getPrice());
        verify(teaRepository, times(1)).findByPriceBetween(min, max);
    }

    @Test
    void addTeaSuccessfully() {
        when(teaRepository.existsByNameIgnoreCase("Green Tea")).thenReturn(false);
        when(teaRepository.save(any(TeaEntity.class))).thenReturn(teaEntity);

        Tea result = teaService.addTea(teaRequest);

        assertNotNull(result);
        assertEquals("Green Tea", result.getName());
        assertEquals(TeaCategory.GREEN, result.getCategory());
        assertEquals(new BigDecimal("5.99"), result.getPrice());
        assertEquals("China", result.getOrigin());
        verify(teaRepository, times(1)).existsByNameIgnoreCase("Green Tea");
        verify(teaRepository, times(1)).save(any(TeaEntity.class));
    }

    @Test
    void throwExceptionWhenTeaNameAlreadyExists() {
        when(teaRepository.existsByNameIgnoreCase("Green Tea")).thenReturn(true);

        assertThrows(DuplicateTeaException.class, () -> teaService.addTea(teaRequest));
        verify(teaRepository, times(1)).existsByNameIgnoreCase("Green Tea");
        verify(teaRepository, never()).save(any(TeaEntity.class));
    }

    @Test
    void updatePriceSuccessfully() {
        PriceUpdate priceUpdate = new PriceUpdate();
        priceUpdate.setNewPrice(new BigDecimal("9.99"));

        TeaEntity updatedEntity = TeaEntity.builder()
                .id(1L)
                .name("Green Tea")
                .category(TeaCategory.GREEN)
                .price(new BigDecimal("9.99"))
                .stockQuantity(100)
                .build();

        when(teaRepository.findById(1L)).thenReturn(Optional.of(teaEntity));
        when(teaRepository.save(any(TeaEntity.class))).thenReturn(updatedEntity);

        Tea result = teaService.updatePrice(1L, priceUpdate);

        assertNotNull(result);
        assertEquals(new BigDecimal("9.99"), result.getPrice());
        verify(teaRepository, times(1)).findById(1L);
        verify(teaRepository, times(1)).save(any(TeaEntity.class));
    }

    @Test
    void throwExceptionWhenTeaNotFoundOnPriceUpdate() {
        PriceUpdate priceUpdate = new PriceUpdate();
        priceUpdate.setNewPrice(new BigDecimal("9.99"));

        when(teaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(TeaNotFoundException.class, () -> teaService.updatePrice(99L, priceUpdate));
        verify(teaRepository, times(1)).findById(99L);
        verify(teaRepository, never()).save(any(TeaEntity.class));
    }
}
