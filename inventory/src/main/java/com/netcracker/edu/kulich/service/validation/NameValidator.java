package com.netcracker.edu.kulich.service.validation;


import com.netcracker.edu.kulich.exception.service.ServiceException;

public interface NameValidator {
    String getForbiddenSymbols();

    int getMinNameLength();

    default void check(String name) {
        if (name.length() < getMinNameLength()) {
            throw new ServiceException("Name \"" + name + "\" is too short.");
        }
        if (name.matches(".*[" + getForbiddenSymbols() + "].*")) {
            throw new ServiceException("Name \"" + name + "\" must not contain symbols {" + getForbiddenSymbols() + "}.");
        }
    }
}
