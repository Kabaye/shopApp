package com.netcracker.edu.kulich.exception.service;

import com.netcracker.edu.kulich.exception.CustomerManagementException;
import org.springframework.http.HttpStatus;

public class ServiceException extends CustomerManagementException {
    public ServiceException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
