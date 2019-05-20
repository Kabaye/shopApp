package com.netcracker.edu.kulich.controller;

import com.netcracker.edu.kulich.entity.OrderPaymentStatusEnum;
import com.netcracker.edu.kulich.exception.InventoryException;
import com.netcracker.edu.kulich.logging.DefaultLogging;
import com.netcracker.edu.kulich.logging.Logging;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@DefaultLogging
public class OrderControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = InventoryException.class)
    @Logging(startMessage = "Handling global controller exceptions...", endMessage = "Global controller exception handled.", level = LogLevel.WARN)
    public ResponseEntity handleConflict(InventoryException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(),
                new HttpHeaders(), ex.getStatus(), request);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    @Logging(startMessage = "Handling global controller exceptions...", endMessage = "Global controller exception handled.", level = LogLevel.WARN)
    public ResponseEntity handleEnumNotCorrectValue(IllegalArgumentException ex, WebRequest request) {
        return handleExceptionInternal(ex, "Order status has invalid value. Please, set valid = {" +
                        OrderPaymentStatusEnum.NOT_PAID.toString().toLowerCase() + ", " +
                        OrderPaymentStatusEnum.PAID.toString().toLowerCase() + "}.",
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
