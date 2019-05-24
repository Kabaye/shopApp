package com.netcracker.edu.kulich.customer.service.validation;

import com.netcracker.edu.kulich.exception.service.ServiceException;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Validator
public class CustomerAgeValidator implements AgeValidator {
    private final Integer minAllowedAge;
    private final Integer maxAllowedAge;
    private final static Logger logger = LoggerFactory.getLogger(AgeValidator.class);

    public CustomerAgeValidator(Integer minAllowedAge, Integer maxAllowedAge) {
        this.minAllowedAge = minAllowedAge;
        this.maxAllowedAge = maxAllowedAge;
    }

    @Override
    public void check(Integer age) {
        if (age < getMinAllowedAge() || age > getMaxAllowedAge()) {
            logger.error("Parameters of customer are invalid.");
            throw new ServiceException("Age of customer is invalid. Age must be within bounds: {" + getMinAllowedAge() + ", "
                    + getMaxAllowedAge() + "}.");
        }
    }
}
