package com.netcracker.edu.kulich.service;

import com.netcracker.edu.kulich.dao.CustomerDAO;
import com.netcracker.edu.kulich.entity.Customer;
import com.netcracker.edu.kulich.exception.InvalidCustomerDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(value = "customerService")
@Transactional
public class CustomerService {
    public static final String INCORRECT_AGE = "Age of customer is incorrect, please, write it properly.";
    public static final String NULL_FIO = "FIO of customer is empty, please set it not empty.";
    private final int MIN_AGE = 14;
    private final int MAX_AGE = 122;

    @Autowired
    private CustomerDAO customerDAO;

    public Customer save(Customer customer) throws InvalidCustomerDataException {
        if (customer.getAge() < MIN_AGE && customer.getAge() > MAX_AGE)
            throw new InvalidCustomerDataException(INCORRECT_AGE);
        if (customer.getFio().equals(""))
            throw new InvalidCustomerDataException(NULL_FIO);
        customer = customerDAO.save(customer);
        return customer;
    }

    public Customer getById(Long id) {
        return customerDAO.getById(id);
    }

    public List<Customer> findAllCustomers() {
        return customerDAO.findAll();
    }

    public Customer updateCustomer(Customer customer) throws InvalidCustomerDataException {
        if (customer.getAge() < MIN_AGE && customer.getAge() > MAX_AGE)
            throw new InvalidCustomerDataException(INCORRECT_AGE);
        if (customer.getFio().equals(""))
            throw new InvalidCustomerDataException(NULL_FIO);
        customer = customerDAO.update(customer);
        return customer;
    }

    public void deleteCustomerById(Long id) {
        customerDAO.deleteById(id);
    }
}
