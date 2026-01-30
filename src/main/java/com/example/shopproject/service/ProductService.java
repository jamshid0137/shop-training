package com.example.shopproject.service;

import com.example.shopproject.dto.CreateProductRequest;
import com.example.shopproject.dto.ProductResponse;
import com.example.shopproject.dto.UpdateProductRequest;
import com.example.shopproject.entity.Product;

import java.util.List;

public interface ProductService {

    ProductResponse create(CreateProductRequest request);

    ProductResponse update(Long id, UpdateProductRequest request);

    ProductResponse getById(Long id);

    List<ProductResponse> getAll();

    void delete(Long id);
    Product getProductEntityById(Long id);
}
