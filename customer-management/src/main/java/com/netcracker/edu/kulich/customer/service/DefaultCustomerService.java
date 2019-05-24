package com.netcracker.edu.kulich.customer.service;

import com.netcracker.edu.kulich.customer.dao.CustomerDAO;
import com.netcracker.edu.kulich.customer.entity.Customer;
import com.netcracker.edu.kulich.customer.service.validation.*;
import com.netcracker.edu.kulich.exception.service.ServiceException;
import com.netcracker.edu.kulich.logging.annotation.DefaultLogging;
import com.netcracker.edu.kulich.logging.annotation.Logging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service(value = "customerService")
@Transactional
@DefaultLogging
public class DefaultCustomerService implements CustomerService {
    private CustomerDAO customerDAO;
    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);
    private EmailValidator customerEmailValidator;
    private AgeValidator customerAgeValidator;
    private NameValidator customerNameValidator;
    private ServiceValidator<Customer, String> customerValidator;

    public DefaultCustomerService(CustomerDAO customerDAO, CustomerValidator customerValidator, EmailValidator customerEmailValidator, AgeValidator customerAgeValidator, NameValidator customerNameValidator) {
        this.customerDAO = customerDAO;
        this.customerValidator = customerValidator;
        this.customerEmailValidator = customerEmailValidator;
        this.customerAgeValidator = customerAgeValidator;
        this.customerNameValidator = customerNameValidator;
    }

    @Logging(startMessage = "Request on saving customer to database is received.", endMessage = "Customer is successfully saved to database.")
    public Customer saveCustomer(Customer customer) {
        customer.fioFixing();
        customerValidator.checkForPersist(customer);
        if (checkIfEmailWasAlreadyUsed(customer.getEmail())) {
            logger.error("Attempt to save customer with not unique e-mail.");
            throw new ServiceException("E-mail: \'" + customer.getEmail() + "\' is already in use.");
        }
        customer = customerDAO.save(customer);
        return customer;
    }

    @Logging(startMessage = "Request on getting customer by id from database is received.", endMessage = "Customer is successfully get from database.")
    public Customer getCustomerById(String email) {
        customerEmailValidator.check(email);
        return customerDAO.getById(email);
    }

    @Logging(startMessage = "Request on getting all customers from database is received.", endMessage = "Customers are successfully get from database.")
    public List<Customer> findAllCustomers() {
        return customerDAO.findAll();
    }

    @Logging(startMessage = "Request on updating customer in database is received.", endMessage = "Customer is successfully updated in database.")
    public Customer updateCustomer(Customer customer) {
        customer.fioFixing();
        customerValidator.checkForUpdate(customer);
        Customer foundedCustomer = customerDAO.getById(customer.getEmail());
        customerValidator.checkFoundById(foundedCustomer);
        if (!customer.getFio().equals("")) {
            customerNameValidator.check(customer.getFio());
            foundedCustomer.setFio(customer.getFio());
        }
        if (customer.getAge() != 0) {
            customerAgeValidator.check(customer.getAge());
            foundedCustomer.setAge(customer.getAge());
        }
        return foundedCustomer;
    }

    @Logging(startMessage = "Request on deleting customer from database is received.", endMessage = "Customer is successfully deleted from database.")
    public void deleteCustomerById(String email) {
        customerEmailValidator.check(email);
        try {
            customerDAO.deleteById(email);
        } catch (EntityNotFoundException exc) {
            logger.error("Attempt to delete not existent customer.");
            throw new ServiceException("Customer with e-mail: \"" + email + "\" doesn't exist. You can't delete not existent customer.");
        }
    }

    private boolean checkIfEmailWasAlreadyUsed(String email) {
        Customer customer = getCustomerById(email);
        return customer != null;
    }
}
