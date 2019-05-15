package com.netcracker.edu.kulich.service.validation;

import com.netcracker.edu.kulich.exception.service.CustomerServiceException;
import lombok.Getter;

@Getter
@Validator
public class CustomerAgeValidator implements AgeValidator {
    private final Integer minAllowedAge;
    private final Integer maxAllowedAge;

    public CustomerAgeValidator(Integer minAllowedAge, Integer maxAllowedAge) {
        this.minAllowedAge = minAllowedAge;
        this.maxAllowedAge = maxAllowedAge;
    }

    @Override
    public void check(Integer age) {
        if (age < getMinAllowedAge() || age > getMaxAllowedAge()) {
            throw new CustomerServiceException("Age of customer is invalid. Age must be within bounds: {" + getMinAllowedAge() + ", "
                    + getMaxAllowedAge() + "}.");
        }
    }
}
