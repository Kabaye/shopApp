package com.netcracker.edu.kulich.customer.dao;

import com.netcracker.edu.kulich.customer.entity.Customer;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public interface CustomerDAO {
    Customer save(Customer customer);

    Customer getById(String email);

    List<Customer> findAll();

    Customer update(Customer customer);

    void deleteById(String email) throws EntityNotFoundException;
}
