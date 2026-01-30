package com.example.shopproject.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {
    private Long id;
    private Long customerId; //solishtirishim garak shorinam

    private LocalDateTime orderDate;
    private String status;
    private BigDecimal totalAmount;
    private String deliveryAddress;
    private List<OrderItemResponseDto> items;
}