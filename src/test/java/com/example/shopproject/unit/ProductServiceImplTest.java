package com.example.shopproject.unit;


import com.example.shopproject.dto.*;
import com.example.shopproject.entity.Category;
import com.example.shopproject.entity.Product;
import com.example.shopproject.mapper.ProductMapper;
import com.example.shopproject.repository.CategoryRepository;
import com.example.shopproject.repository.ProductRepository;
import com.example.shopproject.service.serviceImpl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import com.example.shopproject.exception.NotFoundException;

import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductMapper productMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_shouldSaveProductCorrectly() {
        // Given
        CreateProductRequest request = new CreateProductRequest();
        request.setName("Laptop");
        request.setDescription("Gaming laptop");
        request.setPrice(BigDecimal.valueOf(1200));
        request.setStock(5);
        request.setCategoryId(1L);

        Category category = Category.builder().id(1L).name("Electronics").build();
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .category(category)
                .build();

        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponse responseDto = new ProductResponse();
        responseDto.setName("Laptop");
        when(productMapper.toDto(product)).thenReturn(responseDto);

        // When
        ProductResponse response = productService.create(request);

        // Then
        assertNotNull(response);
        assertEquals("Laptop", response.getName());
        verify(productRepository, times(1)).save(any(Product.class));
        verify(productMapper, times(1)).toDto(product);
    }

    @Test
    void create_shouldThrowNotFoundException_whenCategoryNotExists() {
        CreateProductRequest request = new CreateProductRequest();
        request.setCategoryId(1L);

        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.create(request));

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void update_shouldUpdateProductCorrectly() {
        // Given
        UpdateProductRequest request = new UpdateProductRequest();
        request.setName("Laptop Pro");
        request.setDescription("High-end laptop");
        request.setPrice(BigDecimal.valueOf(2000));
        request.setStock(3);
        request.setCategoryId(2L);

        Product existingProduct = Product.builder().id(1L).build();
        Category newCategory = Category.builder().id(2L).name("Electronics").build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(newCategory));

        when(productRepository.save(existingProduct)).thenReturn(existingProduct);

        ProductResponse responseDto = new ProductResponse();
        responseDto.setName("Laptop Pro");
        when(productMapper.toDto(existingProduct)).thenReturn(responseDto);

        // When
        ProductResponse response = productService.update(1L, request);

        // Then
        assertEquals("Laptop Pro", response.getName());
        verify(productRepository, times(1)).save(existingProduct);
        verify(productMapper, times(1)).toDto(existingProduct);
    }

    @Test
    void getById_shouldReturnProductResponse_whenProductExists() {
        Product product = Product.builder().id(1L).name("Laptop").build();
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductResponse responseDto = new ProductResponse();
        responseDto.setName("Laptop");
        when(productMapper.toDto(product)).thenReturn(responseDto);

        ProductResponse response = productService.getById(1L);

        assertEquals("Laptop", response.getName());
    }

    @Test
    void getById_shouldThrowNotFoundException_whenProductNotExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.getById(1L));
    }


    @Test
    void delete_shouldDeleteProduct_whenExists() {
        when(productRepository.existsById(1L)).thenReturn(true);

        productService.delete(1L);

        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_shouldThrowNotFoundException_whenNotExists() {
        when(productRepository.existsById(1L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> productService.delete(1L));
    }
}
