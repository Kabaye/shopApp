package com.netcracker.edu.kulich.offer.validation;

import com.netcracker.edu.kulich.exception.service.ServiceException;
import com.netcracker.edu.kulich.validation.PriceRangeValidator;
import com.netcracker.edu.kulich.validation.Validator;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Validator
public class OfferPriceRangeValidator implements PriceRangeValidator {
    private static final Logger logger = LoggerFactory.getLogger(PriceRangeValidator.class);
    private final Double minPrice;

    public OfferPriceRangeValidator(Double minPrice) {
        this.minPrice = minPrice;
    }


    @Override
    public void check(Double low, Double high) {
        if (low < getMinPrice() || low > high) {
            logger.error("Price bounds aren't valid.");
            throw new ServiceException("Price bounds are incorrect. Please, set them correct.");
        }
    }
}
