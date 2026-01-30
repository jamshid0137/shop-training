package com.example.shopproject.dto;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CategoryResponse {

    private Long id;
    private String name;
}
