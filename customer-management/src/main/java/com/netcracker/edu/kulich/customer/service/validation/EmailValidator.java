package com.netcracker.edu.kulich.customer.service.validation;

import java.util.regex.Pattern;

public interface EmailValidator {
    Pattern getEmailPattern();

    void check(String email);
}
