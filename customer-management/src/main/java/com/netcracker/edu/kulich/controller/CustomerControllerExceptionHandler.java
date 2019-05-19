package com.netcracker.edu.kulich.controller;

import com.netcracker.edu.kulich.exception.CustomerManagementException;
import com.netcracker.edu.kulich.logging.DefaultLogging;
import com.netcracker.edu.kulich.logging.Logging;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@DefaultLogging
@ControllerAdvice
public class CustomerControllerExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(CustomerManagementException.class)
    @Logging(startMessage = "Handling global controller exceptions...", endMessage = "Global controller exception handled.", level = LogLevel.WARN)
    public ResponseEntity handleControllerConflict(CustomerManagementException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(),
                new HttpHeaders(), ex.getStatus(), request);
    }
}
