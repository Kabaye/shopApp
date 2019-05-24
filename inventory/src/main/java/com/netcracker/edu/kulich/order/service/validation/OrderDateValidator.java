package com.netcracker.edu.kulich.order.service.validation;

import com.netcracker.edu.kulich.exception.service.ServiceException;
import com.netcracker.edu.kulich.validation.DateValidator;
import com.netcracker.edu.kulich.validation.Validator;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

@Getter
@Validator
public class OrderDateValidator implements DateValidator {
    private static final Logger logger = LoggerFactory.getLogger(DateValidator.class);
    private final LocalDate beginning;
    private final LocalDate ending;

    public OrderDateValidator(LocalDate beginning, LocalDate ending) {
        this.beginning = beginning;
        this.ending = ending;
    }

    @Override
    public void check(LocalDate date) {
        if (date == null) {
            logger.error("NULL date.");
            throw new ServiceException("Date can't be null.");
        }
        if (date.isBefore(getBeginning()) || date.isAfter(getEnding())) {
            logger.error("Invalid date bounds.");
            throw new ServiceException("Date can't be before than: " + beginning + ", and later than: " + ending + ";");
        }
    }
}
