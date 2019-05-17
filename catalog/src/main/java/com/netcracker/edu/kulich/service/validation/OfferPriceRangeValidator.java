package com.netcracker.edu.kulich.service.validation;

import lombok.Getter;

@Getter
@Validator
public class OfferPriceRangeValidator implements PriceRangeValidator {
    private final Double minPrice;

    public OfferPriceRangeValidator(Double minPrice) {
        this.minPrice = minPrice;
    }
}
