package com.example.shopproject.unit;



import com.example.shopproject.dto.CreateCustomerRequest;
import com.example.shopproject.dto.CustomerResponse;
import com.example.shopproject.dto.UpdateCustomerRequest;
import com.example.shopproject.entity.Customer;
import com.example.shopproject.exception.NotFoundException;
import com.example.shopproject.repository.CustomerRepository;
import com.example.shopproject.service.serviceImpl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceImplTest {

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void sozlashi() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void creat_uchun() {
        CreateCustomerRequest request = new CreateCustomerRequest();
        request.setName("Jasur");
        request.setEmail("jasur@mail.com");
        request.setPhone("998901234567");
        request.setAddress("Toshkent cithy");

        Customer customer = Customer.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .build();

        when(customerRepository.existsByEmail("jasur@mail.com")).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerResponse javob = customerService.create(request);

        assertEquals("Jasur", javob.getName());
        assertEquals("jasur@mail.com", javob.getEmail());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void creati_email_mavjud_bolsa_xato() {
        CreateCustomerRequest request = new CreateCustomerRequest();
        request.setEmail("jasur@mail.com");

        when(customerRepository.existsByEmail("jasur@mail.com")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> customerService.create(request));
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void updateniki_muvaffaqiyatli() {
        UpdateCustomerRequest request = new UpdateCustomerRequest();
        request.setName("Jasur New");
        request.setEmail("jasurnew@mail.com");
        request.setPhone("998901234568");
        request.setAddress("Samarqandli bola");

        Customer existing = Customer.builder().id(1L).build();
        when(customerRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(customerRepository.save(existing)).thenReturn(existing);

        CustomerResponse javob = customerService.update(1L, request);

        assertEquals("Jasur New", javob.getName());
        verify(customerRepository, times(1)).save(existing);
    }



    @Test
    void olishById_muvaffaqiyatli() {
        Customer customer = Customer.builder().id(1L).name("Jasur").build();
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        CustomerResponse javob = customerService.getById(1L);
        assertEquals("Jasur", javob.getName());
    }

    @Test
    void olishById_mavjud_bolmasa_xato() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> customerService.getById(1L));
    }

    @Test
    void olishHammasi_muvaffaqiyatlimi() {
        Customer c1 = Customer.builder().id(1L).name("Jasur").build();
        Customer c2 = Customer.builder().id(2L).name("Shoxrux").build();

        when(customerRepository.findAll()).thenReturn(List.of(c1, c2));

        var javoblar = customerService.getAll();

        assertEquals(2, javoblar.size());
        assertEquals("Jasur", javoblar.get(0).getName());
        assertEquals("Shoxrux", javoblar.get(1).getName());
    }

    @Test
    void ochirish_muvaffaqiyatlimi() {
        when(customerRepository.existsById(1L)).thenReturn(true);

        customerService.delete(1L);

        verify(customerRepository, times(1)).deleteById(1L);
    }

    @Test
    void ochirish_mavjud_bolmasa_xato() {
        when(customerRepository.existsById(1L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> customerService.delete(1L));
    }
}
