package com.netcracker.edu.kulich.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface NameValidator {
    Logger logger = LoggerFactory.getLogger(NameValidator.class);

    String getForbiddenSymbols();

    int getMinNameLength();

    void check(String name);
}
