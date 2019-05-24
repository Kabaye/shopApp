package com.netcracker.edu.kulich.validation;

public interface NameValidator {
    String getForbiddenSymbols();

    int getMinNameLength();

    void check(String name);
}
