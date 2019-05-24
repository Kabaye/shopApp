package com.netcracker.edu.kulich.validation;

import java.time.LocalDate;

public interface DateValidator {
    LocalDate getBeginning();

    LocalDate getEnding();

    void check(LocalDate date);
}
