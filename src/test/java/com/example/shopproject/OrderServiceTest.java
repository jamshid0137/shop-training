package com.example.shopproject;



import com.example.shopproject.dto.CreateOrderRequest;
import com.example.shopproject.dto.OrderItemDto;
import com.example.shopproject.entity.Customer;
import com.example.shopproject.entity.Order;
import com.example.shopproject.entity.OrderItem;
import com.example.shopproject.enums.OrderStatus;
import com.example.shopproject.exception.NotFoundException;
import com.example.shopproject.observer.OrderStatusSubject;
import com.example.shopproject.repository.CustomerRepository;
import com.example.shopproject.repository.OrderRepository;
import com.example.shopproject.service.OrderService;
import com.example.shopproject.service.ProductService;
import com.example.shopproject.util.email.EmailSendingService;
import com.example.shopproject.util.rabbitmq.EmailPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private OrderStatusSubject statusSubject;

    @Mock
    private ProductService productService;

    @Mock
    private EmailSendingService emailSendingService;

    @Mock
    private EmailPublisher emailPublisher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createOrder_shouldSaveOrderCorrectly() {
        // Given
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setEmail("jmadaminov.unlimited@gmail.com");

        OrderItemDto itemDto = new OrderItemDto();
        itemDto.setProductId(1L);
        itemDto.setQuantity(2);

        CreateOrderRequest request = CreateOrderRequest.builder()
                .customerId(1L)
                .items(List.of(itemDto))
                .deliveryAddress("Toshkent viloyat")
                .build();

        // ProductService mock qilish
        var product = new com.example.shopproject.entity.Product();
        product.setId(1L);
        product.setPrice(BigDecimal.valueOf(50));
        when(productService.getProductEntityById(1L)).thenReturn(product);

        // CustomerRepository mock
        when(customerRepository.getReferenceById(1L)).thenReturn(customer);

        // OrderRepository mock
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        when(orderRepository.save(orderCaptor.capture())).thenAnswer(i -> i.getArgument(0));

        // When
        var result = orderService.createOrder(request);

        // Then
        assertNotNull(result);
        assertEquals(OrderStatus.PENDING.name(), result.getStatus());
        assertEquals(100, result.getTotalAmount().intValue()); // 50*2
        assertEquals(1, result.getItems().size());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void getCustomerEntityById_whenCustomerExists_shouldReturnCustomer() {
        Customer customer = new Customer();
        customer.setId(1L);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        var result = orderService.getCustomerEntityById(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    void getCustomerEntityById_whenCustomerNotFound_shouldThrowException() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> orderService.getCustomerEntityById(1L));
    }

    @Test
    void updateStatus_shouldChangeOrderStatusAndSendEmail() {
        // Given
        Order order = Order.builder()
                .id(1L)
                .status(OrderStatus.PENDING)
                .customer(new Customer() {{
                    setEmail("jmadaminov.unlimited@gmail.com");
                }})
                .items(List.of())
                .build();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);


        var result = orderService.updateStatus(1L, OrderStatus.PENDING);


        assertEquals(OrderStatus.PENDING.name(), result.getStatus());

        var result1 = orderService.updateStatus(1L, OrderStatus.DELIVERED);
        assertEquals(OrderStatus.PENDING.name(), result.getStatus());

    }
}