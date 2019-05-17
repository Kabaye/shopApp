package com.netcracker.edu.kulich.service.validation;

import com.netcracker.edu.kulich.exception.service.ServiceException;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Validator
public class OrderDateValidator implements DateValidator {
    private final LocalDate beginning;
    private final LocalDate ending;

    public OrderDateValidator(LocalDate beginning, LocalDate ending) {
        this.beginning = beginning;
        this.ending = ending;
    }

    @Override
    public void check(LocalDate date) {
        if (date == null) {
            throw new ServiceException("Date can't be null.");
        }
        if (date.isBefore(getBeginning()) || date.isAfter(getEnding())) {
            throw new ServiceException("Date can't be before than: " + beginning + ", and later than: " + ending + ";");
        }
    }
}
