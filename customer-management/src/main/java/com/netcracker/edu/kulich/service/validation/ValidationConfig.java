package com.netcracker.edu.kulich.service.validation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidationConfig {
    private static final Integer DEFAULT_MIN_NAME_LENGTH = 3;
    private static final String DEFAULT_FORBIDDEN_SYMBOLS = "=(){}*";
    private static final Integer DEFAULT_MIN_ALLOWED_AGE = 14;
    private static final Integer DEFAULT_MAX_ALLOWED_AGE = 122;
    private static final String EMAIL_PATTERN = "[a-z0-9]([._]?[a-z0-9]+)*[@][a-z][a-z]*[.]((org)|(net)|(ru)|(com)|(by))";

    @Bean
    public String emailPattern() {
        return EMAIL_PATTERN;
    }

    @Bean
    public Integer minNameLength() {
        return DEFAULT_MIN_NAME_LENGTH;
    }

    @Bean
    public Integer minNameLengthCustomer() {
        return DEFAULT_MIN_NAME_LENGTH;
    }

    @Bean
    public String forbiddenSymbolsCustomer() {
        return DEFAULT_FORBIDDEN_SYMBOLS;
    }

    @Bean
    public Integer minAllowedAge() {
        return DEFAULT_MIN_ALLOWED_AGE;
    }

    @Bean
    public Integer maxAllowedAge() {
        return DEFAULT_MAX_ALLOWED_AGE;
    }
}
