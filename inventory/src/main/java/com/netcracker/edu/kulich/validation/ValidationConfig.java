package com.netcracker.edu.kulich.validation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.regex.Pattern;

@Configuration
public class ValidationConfig {
    private static final Integer DEFAULT_MIN_NAME_LENGTH = 2;
    private static final String DEFAULT_FORBIDDEN_SYMBOLS = "=(){}*;";
    private static final Double MIN_PRICE = 9.99;
    private static final LocalDate BEGINNING = LocalDate.of(2000, 1, 1);
    private static final LocalDate ENDING = LocalDate.now();
    @Bean
    public Pattern emailPattern() {
        final int minUserNameLength = 3;
        return Pattern.compile("([a-z0-9]([._\\-]?[a-z0-9]+)*){" + minUserNameLength + "}[@][a-z][a-z]*[.]((org)|(net)|(ru)|(com)|(by))", Pattern.CASE_INSENSITIVE);
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
