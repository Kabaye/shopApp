package com.netcracker.edu.kulich.service.validation;

public interface EmailValidator {
    String getEmailPattern();

    void check(String email);
}
