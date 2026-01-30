package com.example.shopproject.service;


import com.example.shopproject.dto.CreateOrderRequest;
import com.example.shopproject.dto.OrderItemResponseDto;
import com.example.shopproject.dto.OrderResponseDto;
import com.example.shopproject.entity.Customer;
import com.example.shopproject.entity.Order;
import com.example.shopproject.entity.OrderItem;
import com.example.shopproject.enums.OrderStatus;
import com.example.shopproject.exception.NotFoundException;
import com.example.shopproject.observer.OrderStatusSubject;
import com.example.shopproject.repository.CustomerRepository;
import com.example.shopproject.repository.OrderRepository;
import com.example.shopproject.util.email.EmailSendingService;
import com.example.shopproject.util.rabbitmq.EmailPublisher;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderStatusSubject statusSubject;
    private final ProductService productService;
    private final EmailSendingService emailSendingService;
    private final CustomerRepository customerRepository;
    private final EmailPublisher emailPublisher;

    public Customer getCustomerEntityById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found"));
    }
    @Transactional
    public OrderResponseDto createOrder(CreateOrderRequest request) {
        // CUSTOMERNI TEKSHIRISH YUQ BUOLSA XATO TASHLASHI GARAK
        Customer customer = customerRepository.getReferenceById(request.getCustomerId());

        Order order = Order.builder()
                .customer(customer)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .deliveryAddress(request.getDeliveryAddress())
                .build();


        var items = request.getItems().stream().map(dto -> {
            var product = productService.getProductEntityById(dto.getProductId()); //Tekshirishim kerak ????
            OrderItem item = OrderItem.builder()
                    .productId(dto.getProductId())
                    .quantity(dto.getQuantity())

                    .price(product.getPrice()) // bu yerda real price olish mumkin ProductService orqali shuni olsaq
                    .order(order)
                    .build();
            return item;
        }).collect(Collectors.toList());

        order.setItems(items);
        order.setTotalAmount(items.stream().map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity()))).reduce(BigDecimal.ZERO, BigDecimal::add));

        Order savedOrder = orderRepository.save(order);

        return mapToDto(savedOrder);
    }

    public OrderResponseDto getOrder(Long id) {
        return orderRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public OrderResponseDto updateStatus(Long id, OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        orderRepository.save(order);
        statusSubject.notifyObservers(order.getId(), status.name());
        //
        //emailSendingService.sendSimpleEmail(order.getCustomer().getEmail(),"Update status","Buyurtma statusi o'zgardi: "+status);

        // RabbitMQ orqali email yuborsak
        emailPublisher.sendEmail(
                order.getCustomer().getEmail(),
                "Order Status Update",
                "Sizning buyurtmangiz #" + order.getId() + " statusi o'zgardi: " + status
        );

        return mapToDto(order);
    }

    public List<OrderResponseDto> getByCustomer(Long customerId) {
        return orderRepository.findByCustomerId(customerId).stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private OrderResponseDto mapToDto(Order order) {
        var items = order.getItems().stream().map(i ->
                new OrderItemResponseDto(i.getProductId(), i.getQuantity(), i.getPrice())
        ).collect(Collectors.toList());

        return OrderResponseDto.builder()
                .id(order.getId())

                .orderDate(order.getOrderDate())
                .status(order.getStatus().name())
                .deliveryAddress(order.getDeliveryAddress())
                .totalAmount(order.getTotalAmount())
                .items(items)
                .build();
    }
}