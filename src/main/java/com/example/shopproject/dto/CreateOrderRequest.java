package com.example.shopproject.dto;



import com.example.shopproject.enums.PaymentMethod;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {

    @NotNull
    private Long customerId;

    @NotEmpty
    private List<OrderItemDto> items;

    @NotNull
    private PaymentMethod paymentMethod;

    private String deliveryAddress;
}