package com.netcracker.edu.kulich.service.validation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class ValidationConfig {
    private static final Integer DEFAULT_MIN_NAME_LENGTH = 2;
    private static final String DEFAULT_FORBIDDEN_SYMBOLS = "=(){}*";
    private static final Double MIN_PRICE = 9.99;
    private static final LocalDate BEGINNING = LocalDate.of(2000, 1, 1);
    private static final LocalDate ENDING = LocalDate.now();
    private static final String EMAIL_PATTERN = "[a-z0-9]([._]?[a-z0-9]+)*[@][a-z][a-z]*[.]((org)|(net)|(ru)|(com)|(by))";

    @Bean
    public String emailPattern() {
        return EMAIL_PATTERN;
    }


    @Bean
    public LocalDate beginning() {
        return BEGINNING;
    }

    @Bean
    public LocalDate ending() {
        return ENDING;
    }

    @Bean
    public Double minPrice() {
        return MIN_PRICE;
    }

    @Bean
    public Integer minNameLength() {
        return DEFAULT_MIN_NAME_LENGTH;
    }

    @Bean
    public String forbiddenSymbols() {
        return DEFAULT_FORBIDDEN_SYMBOLS;
    }
}
