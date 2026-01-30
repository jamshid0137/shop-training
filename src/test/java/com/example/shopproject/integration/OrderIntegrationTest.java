package com.example.shopproject.integration;


import com.example.shopproject.dto.CreateOrderRequest;
import com.example.shopproject.dto.OrderItemDto;
import com.example.shopproject.entity.Customer;
import com.example.shopproject.repository.CustomerRepository;
import com.example.shopproject.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional  // Testdan keyin DB ortina qaytadu
class OrderIntegrationTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private com.example.shopproject.service.OrderService orderService;

    private Customer customer;

    @BeforeEach
    void setup() {
        // DB ga test customer qo‘shish
        customer = Customer.builder()
                .name("Jamshid")
                .email("75jmadaminov.unlimited@gmail.com")
                .address("Toshkent")
                .phone("998901234567")
                .build();
        customer = customerRepository.save(customer);
    }

    @Test
    void createOrder_shouldSaveOrderInDatabase() {
        OrderItemDto item = new OrderItemDto();
        item.setProductId(1L);
        item.setQuantity(2);

        CreateOrderRequest request = CreateOrderRequest.builder()
                .customerId(customer.getId())
                .items(List.of(item))
                .deliveryAddress("Chilonzor")
                .build();

        var response = orderService.createOrder(request);

        assertNotNull(response.getId());
        assertEquals(customer.getId(), response.getItems().get(0).getProductId()+5); // bazamda 5 dana order bor shunchun
        assertEquals("Chilonzor", response.getDeliveryAddress());
        assertEquals(3, orderRepository.count()); // DB ga save bo'lganini go'rsam
    }
}
