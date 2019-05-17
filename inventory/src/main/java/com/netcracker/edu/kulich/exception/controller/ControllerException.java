package com.netcracker.edu.kulich.exception.controller;

import com.netcracker.edu.kulich.exception.InventoryException;
import org.springframework.http.HttpStatus;

public class ControllerException extends InventoryException {
    public ControllerException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
