package com.netcracker.edu.kulich.controller;

import com.netcracker.edu.kulich.exception.CatalogException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    private final String MESSAGE = "You need to send 2 prices only as one object.";

    @ExceptionHandler(value = CatalogException.class)
    private ResponseEntity handleConflict(CatalogException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(),
                new HttpHeaders(), ex.getStatus(), request);
    }

    @ExceptionHandler(value = ClassCastException.class)
    private ResponseEntity handleCastConglict(ClassCastException ex, WebRequest request) {
        return handleExceptionInternal(ex, MESSAGE, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
