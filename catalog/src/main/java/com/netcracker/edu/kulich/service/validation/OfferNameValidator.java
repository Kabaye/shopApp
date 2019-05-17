package com.netcracker.edu.kulich.service.validation;

import lombok.Getter;

@Getter
@Validator
public class OfferNameValidator implements NameValidator {
    private final String forbiddenSymbols;
    private final int minNameLength;

    public OfferNameValidator(String forbiddenSymbols, int minNameLength) {
        this.forbiddenSymbols = forbiddenSymbols;
        this.minNameLength = minNameLength;
    }
}
