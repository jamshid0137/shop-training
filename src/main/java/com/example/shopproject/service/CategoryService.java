package com.example.shopproject.service;


import com.example.shopproject.dto.CategoryResponse;
import com.example.shopproject.dto.CreateCategoryRequest;

import java.util.List;

public interface CategoryService {

    CategoryResponse create(CreateCategoryRequest request);

    List<CategoryResponse> getAll();
}
