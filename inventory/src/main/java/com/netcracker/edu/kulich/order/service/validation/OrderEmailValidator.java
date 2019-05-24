package com.netcracker.edu.kulich.order.service.validation;

import com.netcracker.edu.kulich.exception.service.ServiceException;
import com.netcracker.edu.kulich.validation.EmailValidator;
import com.netcracker.edu.kulich.validation.Validator;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Validator
public class OrderEmailValidator implements EmailValidator {
    private final Pattern emailPattern;
    private final static Logger logger = LoggerFactory.getLogger(EmailValidator.class);

    public OrderEmailValidator(Pattern emailPattern) {
        this.emailPattern = emailPattern;
    }

    @Override
    public void check(String email) {
        Matcher matcher = emailPattern.matcher(email);
        if (!matcher.matches()) {
            logger.error("Parameters of customer are invalid.");
            throw new ServiceException("E-mail \"" + email + "\" is invalid");
        }
    }
}
