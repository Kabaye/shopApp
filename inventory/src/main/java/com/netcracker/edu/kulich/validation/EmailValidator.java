package com.netcracker.edu.kulich.validation;

import java.util.regex.Pattern;

public interface EmailValidator {
    Pattern getEmailPattern();

    void check(String email);
}
