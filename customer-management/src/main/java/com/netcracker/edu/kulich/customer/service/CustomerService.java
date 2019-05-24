package com.netcracker.edu.kulich.customer.service;

import com.netcracker.edu.kulich.customer.entity.Customer;

import java.util.List;

public interface CustomerService {
    Customer saveCustomer(Customer customer);

    Customer getCustomerById(String email);

    List<Customer> findAllCustomers();

    Customer updateCustomer(Customer customer);

    void deleteCustomerById(String email);
}
