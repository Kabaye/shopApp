package com.netcracker.edu.kulich.validation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidationConfig {
    private static final Integer DEFAULT_MIN_NAME_LENGTH = 2;
    private static final String DEFAULT_FORBIDDEN_SYMBOLS = "=(){}*;";
    private static final Double MIN_PRICE = 9.99;

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
