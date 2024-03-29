package com.netcracker.edu.kulich.customer.service.validation;

import com.netcracker.edu.kulich.exception.service.ServiceException;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Validator
public class CustomerNameValidator implements NameValidator {
    private final String forbiddenSymbols;
    private final Integer minNameLength;
    private final static Logger logger = LoggerFactory.getLogger(NameValidator.class);

    public CustomerNameValidator(String forbiddenSymbolsCustomer, Integer minNameLengthCustomer) {
        this.forbiddenSymbols = forbiddenSymbolsCustomer;
        this.minNameLength = minNameLengthCustomer;
    }

    @Override
    public void check(String name) {
        if (name == null || name.length() < getMinNameLength()) {
            logger.error("Parameters of customer are invalid.");
            throw new ServiceException("Name \"" + name + "\" is too short.");
        }
        if (name.matches(".*[" + getForbiddenSymbols() + "].*")) {
            logger.error("Parameters of customer are invalid.");
            throw new ServiceException(name + " must not contain symbols {" + getForbiddenSymbols() + "}");
        }
    }
}
