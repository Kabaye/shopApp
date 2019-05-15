package com.netcracker.edu.kulich.service;

import com.netcracker.edu.kulich.dao.CustomerDAO;
import com.netcracker.edu.kulich.entity.Customer;
import com.netcracker.edu.kulich.exception.service.CustomerServiceException;
import com.netcracker.edu.kulich.service.validation.AgeValidator;
import com.netcracker.edu.kulich.service.validation.CustomerValidator;
import com.netcracker.edu.kulich.service.validation.EmailValidator;
import com.netcracker.edu.kulich.service.validation.NameValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service(value = "customerService")
@Transactional
public class DefaultCustomerService implements CustomerService {
    private CustomerDAO customerDAO;
    private CustomerValidator customerValidator;
    private EmailValidator customerEmailValidator;
    private AgeValidator customerAgeValidator;
    private NameValidator customerNameValidator;

    public DefaultCustomerService(CustomerDAO customerDAO, CustomerValidator customerValidator, EmailValidator customerEmailValidator, AgeValidator customerAgeValidator, NameValidator customerNameValidator) {
        this.customerDAO = customerDAO;
        this.customerValidator = customerValidator;
        this.customerEmailValidator = customerEmailValidator;
        this.customerAgeValidator = customerAgeValidator;
        this.customerNameValidator = customerNameValidator;
    }

    public Customer saveCustomer(Customer customer) {
        customer.fioFixing();
        customerValidator.checkForPersist(customer);
        if (checkIfEmailWasAlreadyUsed(customer.getEmail())) {
            throw new CustomerServiceException("E-mail: \'" + customer.getEmail() + "\' is already in use.");
        }
        customer = customerDAO.save(customer);
        return customer;
    }

    public Customer getCustomerById(String email) {
        customerEmailValidator.check(email);
        return customerDAO.getById(email);
    }

    public List<Customer> findAllCustomers() {
        return customerDAO.findAll();
    }

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

    public void deleteCustomerById(String email) {
        try {
            customerDAO.deleteById(email);
        } catch (EntityNotFoundException exc) {
            throw new CustomerServiceException("Customer with e-mail: \"" + email + "\" doesn't exist. You can't delete not existent customer.");
        }
    }

    private boolean checkIfEmailWasAlreadyUsed(String email) {
        Customer customer = getCustomerById(email);
        return customer != null;
    }
}
