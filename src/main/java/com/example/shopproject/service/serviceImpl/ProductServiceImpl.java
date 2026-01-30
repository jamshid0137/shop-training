package com.example.shopproject.service.serviceImpl;


import com.example.shopproject.dto.CreateProductRequest;
import com.example.shopproject.dto.ProductResponse;
import com.example.shopproject.dto.UpdateProductRequest;
import com.example.shopproject.entity.Category;
import com.example.shopproject.entity.Product;
import com.example.shopproject.exception.NotFoundException;
import com.example.shopproject.mapper.ProductMapper;
import com.example.shopproject.repository.CategoryRepository;
import com.example.shopproject.repository.ProductRepository;
import com.example.shopproject.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductResponse create(CreateProductRequest request) {

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found"));

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .category(category)
                .build();

        return productMapper.toDto(productRepository.save(product));
    }

    @Override
    public ProductResponse update(Long id, UpdateProductRequest request) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found"));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCategory(category);

        return productMapper.toDto(productRepository.save(product));
    }

    @Override
    public ProductResponse getById(Long id) {
        return productMapper.toDto(
                productRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Product not found"))
        );
    }

    @Override
    public List<ProductResponse> getAll() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toDto)
                .toList();
    }

    @Override
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new NotFoundException("Product not found");
        }
        productRepository.deleteById(id);
    }

    @Override
    public Product getProductEntityById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
    }

}
