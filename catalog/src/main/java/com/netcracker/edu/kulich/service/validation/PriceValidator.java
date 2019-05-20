package com.netcracker.edu.kulich.service.validation;


import com.netcracker.edu.kulich.entity.Price;
import com.netcracker.edu.kulich.exception.service.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface PriceValidator {
    Logger logger = LoggerFactory.getLogger(PriceValidator.class);
    Double getMinPrice();

    default void check(Price price) {
        if (price.getPrice() < getMinPrice()) {
            logger.error("Price isn't correct.");
            throw new ServiceException("price " + price.getPrice() + " must be >= " + getMinPrice());
        }
    }
}
