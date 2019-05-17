package com.netcracker.edu.kulich.service.validation;


import com.netcracker.edu.kulich.entity.Price;
import com.netcracker.edu.kulich.exception.service.ServiceException;

public interface PriceValidator {
    Double getMinPrice();

    default <P extends Price> void check(P price) {
        if (price.getPrice() < getMinPrice()) {
            throw new ServiceException("price " + price.getPrice() + " must be >= " + getMinPrice());
        }
    }
}
