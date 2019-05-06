package com.netcracker.edu.kulich.exception.service;

import com.netcracker.edu.kulich.exception.CustomerManagementException;
import org.springframework.http.HttpStatus;

public class CustomerServiceException extends CustomerManagementException {
    public CustomerServiceException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
