package com.netcracker.edu.kulich.offer.validation;

import com.netcracker.edu.kulich.exception.service.ServiceException;
import com.netcracker.edu.kulich.logging.annotation.DefaultLogging;
import com.netcracker.edu.kulich.logging.annotation.Logging;
import com.netcracker.edu.kulich.validation.NameValidator;
import com.netcracker.edu.kulich.validation.Validator;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Validator
@DefaultLogging
public class OfferNameValidator implements NameValidator {
    private static final Logger logger = LoggerFactory.getLogger(NameValidator.class);
    private final String forbiddenSymbols;
    private final int minNameLength;

    public OfferNameValidator(String forbiddenSymbols, int minNameLength) {
        this.forbiddenSymbols = forbiddenSymbols;
        this.minNameLength = minNameLength;
    }

    @Override
    @Logging(startMessage = "Checking name...", endMessage = "Name checked. Everything correct.")
    public void check(String name) {
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
