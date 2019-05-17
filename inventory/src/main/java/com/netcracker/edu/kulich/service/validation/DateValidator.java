package com.netcracker.edu.kulich.service.validation;

import java.time.LocalDate;

public interface DateValidator {
    LocalDate getBeginning();

    LocalDate getEnding();

    void check(LocalDate date);
}
