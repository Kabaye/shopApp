package com.netcracker.edu.kulich.exception.controller;

import com.netcracker.edu.kulich.exception.CustomerManagementException;
import org.springframework.http.HttpStatus;

public class CustomerControllerException extends CustomerManagementException {
    public CustomerControllerException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
