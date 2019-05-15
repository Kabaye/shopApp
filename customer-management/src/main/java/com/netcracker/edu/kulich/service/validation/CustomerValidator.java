package com.netcracker.edu.kulich.service.validation;

import com.netcracker.edu.kulich.entity.Customer;
import com.netcracker.edu.kulich.exception.service.CustomerServiceException;

@Validator
public class CustomerValidator implements ServiceValidator<Customer, String> {
    private static final String EMAIL_NOT_NULL = "E-mail must be initialized.";
    private static final String CUSTOMER_DOESNT_EXIST = "Customer with such e-mail doesn't exist. Please, set e-mail valid.";
    private static final String CUSTOMER_NOT_NULL = "Customer can't be null.";

    private CustomerAgeValidator customerAgeValidator;
    private CustomerEmailValidator customerEmailValidator;
    private CustomerNameValidator customerNameValidator;

    public CustomerValidator(CustomerAgeValidator customerAgeValidator, CustomerEmailValidator customerEmailValidator, CustomerNameValidator customerNameValidator) {
        this.customerAgeValidator = customerAgeValidator;
        this.customerEmailValidator = customerEmailValidator;
        this.customerNameValidator = customerNameValidator;
    }

    @Override
    public String extractId(Customer resource) {
        return resource.getEmail();
    }

    @Override
    public void checkIdIsNotNull(String id) {
        if (id == null) {
            throw new CustomerServiceException(EMAIL_NOT_NULL);
        }
    }

    @Override
    public void checkNotNull(Customer resource) {
        if (resource == null) {
            throw new CustomerServiceException(CUSTOMER_NOT_NULL);
        }
    }

    @Override
    public void checkNotNull(Customer resource, String errorMessage) {
        if (resource == null) {
            throw new CustomerServiceException(errorMessage);
        }
    }

    @Override
    public void checkFoundById(Customer resource) {
        checkNotNull(resource, CUSTOMER_DOESNT_EXIST);
    }

    @Override
    public void checkProperties(Customer resource) {
        customerEmailValidator.check(resource.getEmail());
        customerAgeValidator.check(resource.getAge());
        customerNameValidator.check(resource.getFio());
    }

}
