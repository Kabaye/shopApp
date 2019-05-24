package com.netcracker.edu.kulich.customer.service.validation;

public interface NameValidator {
    String getForbiddenSymbols();

    Integer getMinNameLength();

    void check(String name);
}
