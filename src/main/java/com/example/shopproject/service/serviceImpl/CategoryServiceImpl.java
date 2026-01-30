package com.example.shopproject.service.serviceImpl;


import com.example.shopproject.dto.CategoryResponse;
import com.example.shopproject.dto.CreateCategoryRequest;
import com.example.shopproject.entity.Category;
import com.example.shopproject.exception.BadRequestException;
import com.example.shopproject.repository.CategoryRepository;
import com.example.shopproject.service.CategoryService;
import lombok.RequiredArgsConstructor;
//import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResponse create(CreateCategoryRequest request) {

        if (categoryRepository.existsByNameIgnoreCase(request.getName())) {
            throw new BadRequestException("Category already exists");
        }

        Category category = Category.builder()
                .name(request.getName())
                .build();

        Category saved = categoryRepository.save(category);

        return CategoryResponse.builder()
                .id(saved.getId())
                .name(saved.getName())
                .build();
    }

    @Override
    public List<CategoryResponse> getAll() {
        return categoryRepository.findAll()
                .stream()
                .map(c -> CategoryResponse.builder()
                        .id(c.getId())
                        .name(c.getName())
                        .build())
                .toList();
    }
}

