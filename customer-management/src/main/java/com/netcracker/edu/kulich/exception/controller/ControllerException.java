package com.netcracker.edu.kulich.exception.controller;

import com.netcracker.edu.kulich.exception.CustomerManagementException;
import org.springframework.http.HttpStatus;

public class ControllerException extends CustomerManagementException {
    public ControllerException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
