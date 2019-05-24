package com.netcracker.edu.kulich.offer.validation;

import com.netcracker.edu.kulich.exception.service.ServiceException;
import com.netcracker.edu.kulich.offer.entity.Price;
import com.netcracker.edu.kulich.validation.PriceValidator;
import com.netcracker.edu.kulich.validation.Validator;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Validator
public class OfferPriceValidator implements PriceValidator {
    private final static Logger logger = LoggerFactory.getLogger(PriceValidator.class);
    private final Double minPrice;

    public OfferPriceValidator(Double minPrice) {
        this.minPrice = minPrice;
    }

    @Override
    public void check(Price price) {
        if (price.getPrice() < getMinPrice()) {
            logger.error("Price isn't correct.");
            throw new ServiceException("price " + price.getPrice() + " must be >= " + getMinPrice());
        }
    }
}
