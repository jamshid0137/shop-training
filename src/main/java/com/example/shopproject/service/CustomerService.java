package com.example.shopproject.service;


import com.example.shopproject.dto.CreateCustomerRequest;
import com.example.shopproject.dto.CustomerResponse;
import com.example.shopproject.dto.UpdateCustomerRequest;

import java.util.List;

public interface CustomerService {
    CustomerResponse create(CreateCustomerRequest request);
    CustomerResponse update(Long id, UpdateCustomerRequest request);
    CustomerResponse getById(Long id);
    List<CustomerResponse> getAll();
    void delete(Long id);
}

