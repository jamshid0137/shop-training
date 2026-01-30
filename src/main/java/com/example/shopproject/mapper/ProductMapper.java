package com.example.shopproject.mapper;


import com.example.shopproject.dto.ProductResponse;
import com.example.shopproject.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    ProductResponse toDto(Product product);
}
