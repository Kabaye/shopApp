package com.netcracker.edu.kulich.service;

import com.netcracker.edu.kulich.entity.Customer;

import java.util.List;

public interface CustomerService {
    Customer saveCustomer(Customer customer);

    Customer getCustomerById(Long id);

    List<Customer> findAllCustomers();

    Customer updateCustomer(Customer customer);

    void deleteCustomerById(Long id);
}
