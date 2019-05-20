package com.netcracker.edu.kulich.service.validation;

import com.netcracker.edu.kulich.exception.service.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface PriceRangeValidator {
    Logger logger = LoggerFactory.getLogger(PriceRangeValidator.class);
    Double getMinPrice();

    default void check(Double low, Double high) {
        if (low < getMinPrice() || low > high) {
            logger.error("Price bounds aren't valid.");
            throw new ServiceException("Price bounds are incorrect. Please, set them correct.");
        }
    }
}
