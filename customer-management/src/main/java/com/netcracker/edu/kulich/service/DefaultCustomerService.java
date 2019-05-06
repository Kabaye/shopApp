package com.netcracker.edu.kulich.service;

import com.netcracker.edu.kulich.dao.CustomerDAO;
import com.netcracker.edu.kulich.entity.Customer;
import com.netcracker.edu.kulich.exception.service.CustomerServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service(value = "customerService")
@Transactional
public class DefaultCustomerService implements CustomerService {
    private static final String INVALID_PARAMS = "Parameters of customer are invalid, please, set them valid!";
    private static final String TRYING_TO_DO_SMTH_WITH_NOT_EXISTENT_CUSTOMER = "You try to delete / update not existent customer!";
    private static final int MIN_AGE = 14;
    private static final int MAX_AGE = 122;

    @Autowired
    private CustomerDAO customerDAO;

    public Customer saveCustomer(Customer customer) {
        customerChecking(customer);
        customer = customerDAO.save(customer);
        return customer;
    }

    public Customer getCustomerById(Long id) {
        return customerDAO.getById(id);
    }

    public List<Customer> findAllCustomers() {
        return customerDAO.findAll();
    }

    public Customer updateCustomer(Customer customer) {
        if (customerDAO.getById(customer.getId()) == null) {
            throw new CustomerServiceException(TRYING_TO_DO_SMTH_WITH_NOT_EXISTENT_CUSTOMER);
        }
        customerChecking(customer);
        customer = customerDAO.update(customer);
        return customer;
    }

    public void deleteCustomerById(Long id) {
        try {
            customerDAO.deleteById(id);
        } catch (EntityNotFoundException exc) {
            throw new CustomerServiceException(TRYING_TO_DO_SMTH_WITH_NOT_EXISTENT_CUSTOMER);
        }
    }

    private void customerChecking(Customer customer) {
        if (customer.getAge() < MIN_AGE || customer.getAge() > MAX_AGE || customer.getFio().equals(""))
            throw new CustomerServiceException(INVALID_PARAMS);
    }
}
