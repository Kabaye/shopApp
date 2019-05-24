package com.netcracker.edu.kulich.customer.service.validation;

public interface AgeValidator {
    Integer getMinAllowedAge();

    Integer getMaxAllowedAge();

    void check(Integer age);
}
