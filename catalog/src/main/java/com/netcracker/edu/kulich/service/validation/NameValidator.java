package com.netcracker.edu.kulich.service.validation;

import com.netcracker.edu.kulich.exception.service.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface NameValidator {
    Logger logger = LoggerFactory.getLogger(NameValidator.class);
    String getForbiddenSymbols();

    int getMinNameLength();

    default void check(String name) {
        if (name.length() < getMinNameLength()) {
            logger.error("Name isn't correct.");
            throw new ServiceException("Name \"" + name + "\" is too short.");
        }
        if (name.matches(".*[" + getForbiddenSymbols() + "].*")) {
            logger.error("Name isn't correct.");
            throw new ServiceException("Name \"" + name + "\" must not contain symbols \"" + getForbiddenSymbols() + "\".");
        }
    }
}
