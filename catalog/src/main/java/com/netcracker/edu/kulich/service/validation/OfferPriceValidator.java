package com.netcracker.edu.kulich.service.validation;

import lombok.Getter;

@Getter
@Validator
public class OfferPriceValidator implements PriceValidator {
    private final Double minPrice;

    public OfferPriceValidator(Double minPrice) {
        this.minPrice = minPrice;
    }
}
