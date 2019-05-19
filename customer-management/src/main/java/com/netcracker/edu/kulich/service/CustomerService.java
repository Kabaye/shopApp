package com.netcracker.edu.kulich.service;

import com.netcracker.edu.kulich.entity.Customer;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface CustomerService {
    Customer saveCustomer(Customer customer);

    Customer getCustomerById(String email);

    List<Customer> findAllCustomers();

    Customer updateCustomer(Customer customer);

    void deleteCustomerById(String email);
}
