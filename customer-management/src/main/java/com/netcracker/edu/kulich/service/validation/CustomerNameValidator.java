package com.netcracker.edu.kulich.service.validation;

import com.netcracker.edu.kulich.exception.service.CustomerServiceException;
import lombok.Getter;

@Getter
@Validator
public class CustomerNameValidator implements NameValidator {
    private final String forbiddenSymbols;
    private final Integer minNameLength;

    public CustomerNameValidator(String forbiddenSymbolsCustomer, Integer minNameLengthCustomer) {
        this.forbiddenSymbols = forbiddenSymbolsCustomer;
        this.minNameLength = minNameLengthCustomer;
    }

    @Override
    public void check(String name) {
        if (name == null || name.length() < getMinNameLength()) {
            throw new CustomerServiceException("Name \"" + name + "\" is too short.");
        }
        if (name.matches(".*[" + getForbiddenSymbols() + "].*")) {
            throw new CustomerServiceException(name + "must not contain symbols {" + getForbiddenSymbols() + "}");
        }
    }
}
