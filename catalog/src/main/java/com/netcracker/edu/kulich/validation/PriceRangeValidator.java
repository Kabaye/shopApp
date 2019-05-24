package com.netcracker.edu.kulich.validation;

public interface PriceRangeValidator {
    Double getMinPrice();

    void check(Double low, Double high);
}
