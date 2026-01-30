package com.example.shopproject;



import com.example.shopproject.dto.CategoryResponse;
import com.example.shopproject.dto.CreateCategoryRequest;
import com.example.shopproject.entity.Category;
import com.example.shopproject.exception.BadRequestException;
import com.example.shopproject.repository.CategoryRepository;
import com.example.shopproject.service.serviceImpl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceImplTest {

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_nomi_yuq_bulsac() {
        // Given
        CreateCategoryRequest request = new CreateCategoryRequest();
        request.setName("Electronikala");

        when(categoryRepository.existsByNameIgnoreCase("Electronikala")).thenReturn(false);

        Category savedCategory = Category.builder()
                .id(1L)
                .name("Electronikala")
                .build();

        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);

        // When
        CategoryResponse response = categoryService.create(request);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Electronikala", response.getName());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void create_testi() {
        // Given
        CreateCategoryRequest request = new CreateCategoryRequest();
        request.setName("Electronikala");

        when(categoryRepository.existsByNameIgnoreCase("Electronikala")).thenReturn(true);

        // Then
        assertThrows(BadRequestException.class, () -> categoryService.create(request));

        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void getAll_responsini() {

        Category cat1 = Category.builder().id(1L).name("Electronikalar").build();
        Category cat2 = Category.builder().id(2L).name("Books").build();

        when(categoryRepository.findAll()).thenReturn(List.of(cat1, cat2));


        List<CategoryResponse> responses = categoryService.getAll();


        assertEquals(2, responses.size());
        assertEquals("Electronikalar", responses.get(0).getName());
        assertEquals("Books", responses.get(1).getName());
        verify(categoryRepository, times(1)).findAll();
    }
}
