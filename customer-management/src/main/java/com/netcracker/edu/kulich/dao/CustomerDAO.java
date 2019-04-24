package com.netcracker.edu.kulich.dao;

import com.netcracker.edu.kulich.entity.Customer;

import java.util.List;

public interface CustomerDAO {
    Customer save(Customer customer);

    Customer getById(Long id);
    List<Customer> findAll();
    Customer update(Customer customer);

    void deleteById(Long id);
}
