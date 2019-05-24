package com.netcracker.edu.kulich.customer.service.validation;

import com.netcracker.edu.kulich.customer.entity.Customer;
import com.netcracker.edu.kulich.exception.service.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Validator
public class CustomerValidator implements ServiceValidator<Customer, String> {
    private static final String EMAIL_NOT_NULL = "E-mail must be initialized.";
    private static final String CUSTOMER_DOESNT_EXIST = "Customer with such e-mail doesn't exist. Please, set e-mail valid.";
    private static final String CUSTOMER_NOT_NULL = "Customer can't be null.";
    private final static Logger logger = LoggerFactory.getLogger(ServiceValidator.class);

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
            logger.error("Id of customer is NULL.");
            throw new ServiceException(EMAIL_NOT_NULL);
        }
    }

    @Override
    public void checkNotNull(Customer resource) {
        if (resource == null) {
            logger.error("Customer is NULL.");
            throw new ServiceException(CUSTOMER_NOT_NULL);
        }
    }

    @Override
    public void checkNotNull(Customer resource, String errorMessage) {
        if (resource == null) {
            logger.error("Customer is NULL");
            throw new ServiceException(errorMessage);
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
