package com.netcracker.edu.kulich.tag.validation;

import com.netcracker.edu.kulich.exception.service.ServiceException;
import com.netcracker.edu.kulich.validation.NameValidator;
import com.netcracker.edu.kulich.validation.Validator;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Validator
public class TagNameValidator implements NameValidator {
    private static final Logger logger = LoggerFactory.getLogger(NameValidator.class);
    private final String forbiddenSymbols;
    private final int minNameLength;

    public TagNameValidator(String forbiddenSymbolsTag, int minNameLengthTag) {
        this.forbiddenSymbols = forbiddenSymbolsTag;
        this.minNameLength = minNameLengthTag;
    }

    @Override
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
