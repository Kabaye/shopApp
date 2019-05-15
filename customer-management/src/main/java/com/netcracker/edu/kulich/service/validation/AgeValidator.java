package com.netcracker.edu.kulich.service.validation;

public interface AgeValidator {
    Integer getMinAllowedAge();

    Integer getMaxAllowedAge();

    void check(Integer age);
}
