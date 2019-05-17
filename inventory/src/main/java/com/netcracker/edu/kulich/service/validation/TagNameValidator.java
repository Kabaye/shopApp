package com.netcracker.edu.kulich.service.validation;

import lombok.Getter;

@Getter
@Validator
public class TagNameValidator implements NameValidator {
    private final String forbiddenSymbols;
    private final int minNameLength;

    public TagNameValidator(String forbiddenSymbols, int minNameLengthTag) {
        this.forbiddenSymbols = forbiddenSymbols;
        this.minNameLength = minNameLengthTag;
    }
}
