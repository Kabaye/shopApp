package com.netcracker.edu.kulich.service;

import com.netcracker.edu.kulich.dao.CustomerDAO;
import com.netcracker.edu.kulich.entity.Customer;
import com.netcracker.edu.kulich.service.exception.CustomerServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service(value = "customerService")
@Transactional
public class DefaultCustomerService implements CustomerService {
    private final String INCORRECT_AGE = "Age of customer is incorrect, please, write it properly.";
    private final String NULL_FIO = "FIO of customer is empty, please set it not empty.";
    private final String DELETING_NOT_EXISTENT_CUSTOMER = "You try to delete not existent customer!";
    private final int MIN_AGE = 14;
    private final int MAX_AGE = 122;

    @Autowired
    private CustomerDAO customerDAO;

    public Customer saveCustomer(Customer customer) throws CustomerServiceException {
        if (customer.getAge() < MIN_AGE && customer.getAge() > MAX_AGE)
            throw new CustomerServiceException(INCORRECT_AGE);
        if (customer.getFio().equals(""))
            throw new CustomerServiceException(NULL_FIO);
        customer = customerDAO.save(customer);
        return customer;
    }

    public Customer getCustomerById(Long id) {
        return customerDAO.getById(id);
    }

    public List<Customer> findAllCustomers() {
        return customerDAO.findAll();
    }

    public Customer updateCustomer(Customer customer) throws CustomerServiceException {
        if (customer.getAge() < MIN_AGE && customer.getAge() > MAX_AGE)
            throw new CustomerServiceException(INCORRECT_AGE);
        if (customer.getFio().equals(""))
            throw new CustomerServiceException(NULL_FIO);
        customer = customerDAO.update(customer);
        return customer;
    }

    public void deleteCustomerById(Long id) throws CustomerServiceException {
        try {
            customerDAO.deleteById(id);
        } catch (EntityNotFoundException exc) {
            throw new CustomerServiceException(DELETING_NOT_EXISTENT_CUSTOMER);
        }
    }
}
