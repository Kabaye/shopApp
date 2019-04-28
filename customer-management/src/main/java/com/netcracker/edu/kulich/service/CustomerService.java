package com.netcracker.edu.kulich.service;

import com.netcracker.edu.kulich.entity.Customer;
import com.netcracker.edu.kulich.service.exception.CustomerServiceException;

import java.util.List;

public interface CustomerService {
    Customer saveCustomer(Customer customer) throws CustomerServiceException;

    Customer getCustomerById(Long id);

    List<Customer> findAllCustomers();

    Customer updateCustomer(Customer customer) throws CustomerServiceException;

    void deleteCustomerById(Long id) throws CustomerServiceException;
}
