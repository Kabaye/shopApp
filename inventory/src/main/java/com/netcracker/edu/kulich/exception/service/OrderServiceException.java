package com.netcracker.edu.kulich.exception.service;

import com.netcracker.edu.kulich.exception.InventoryException;
import org.springframework.http.HttpStatus;

public class OrderServiceException extends InventoryException {
    public OrderServiceException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
