package com.netcracker.edu.kulich.dao;

import com.netcracker.edu.kulich.entity.Customer;

import java.util.List;

public interface CustomerDAO {
    Customer create(Customer customer);
    Customer read(Long id);
    List<Customer> findAll();
    Customer update(Customer customer);
    void delete(Long id);
}
