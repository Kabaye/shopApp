package com.netcracker.edu.kulich.validation;


import com.netcracker.edu.kulich.offer.entity.Price;

public interface PriceValidator {
    Double getMinPrice();

    void check(Price price);
}
