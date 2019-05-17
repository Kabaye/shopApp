package com.netcracker.edu.kulich.controller;

import com.netcracker.edu.kulich.exception.InventoryException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class OrderControllerExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = InventoryException.class)
    private ResponseEntity handleConflict(InventoryException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(),
                new HttpHeaders(), ex.getStatus(), request);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    private ResponseEntity handleEnumUncorrectValue(IllegalArgumentException ex, WebRequest request) {
        return handleExceptionInternal(ex, "Order status has invalid value. Please, set valid.",
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
