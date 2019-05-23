package com.netcracker.edu.kulich.service.validation;

import java.util.regex.Pattern;

public interface EmailValidator {
    Pattern getEmailPattern();

    void check(String email);
}
