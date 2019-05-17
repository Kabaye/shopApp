package com.netcracker.edu.kulich.service.validation;

import com.netcracker.edu.kulich.exception.service.ServiceException;

public interface PriceRangeValidator {
    Double getMinPrice();

    default void check(Double low, Double high) {
        if (low < getMinPrice() || low > high) {
            throw new ServiceException("Price bounds are incorrect. Please, set them correct.");
        }
    }
}
