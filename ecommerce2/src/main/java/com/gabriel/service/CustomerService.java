package com.gabriel.service;

import com.gabriel.model.Customer;

import java.util.List;

public interface CustomerService {
    List<Customer> getAll();
    Customer get(Integer id);
    Customer create(Customer customer);
    Customer update(Customer customer);
    void delete(Integer id);
}

