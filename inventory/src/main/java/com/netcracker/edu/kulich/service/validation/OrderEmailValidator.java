package com.netcracker.edu.kulich.service.validation;

import com.netcracker.edu.kulich.exception.service.ServiceException;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Validator
public class OrderEmailValidator implements EmailValidator {
    private final String emailPattern;
    private final static Logger logger = LoggerFactory.getLogger(EmailValidator.class);

    public OrderEmailValidator(String emailPattern) {
        this.emailPattern = emailPattern;
    }

    @Override
    public void check(String email) {
        Pattern pattern = Pattern.compile(getEmailPattern(), Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            logger.error("Parameters of customer are invalid.");
            throw new ServiceException("E-mail \"" + email + "\" is invalid");
        }
    }
}
