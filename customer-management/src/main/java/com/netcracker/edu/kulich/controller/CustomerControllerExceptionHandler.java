package com.netcracker.edu.kulich.controller;

import com.netcracker.edu.kulich.exception.CustomerManagementException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class CustomerControllerExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = CustomerManagementException.class)
    private ResponseEntity handleConflict(CustomerManagementException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(),
                new HttpHeaders(), ex.getStatus(), request);
    }
}
