package com.netcracker.edu.kulich.service.validation;

public interface NameValidator {
    String getForbiddenSymbols();

    Integer getMinNameLength();

    void check(String name);
}
