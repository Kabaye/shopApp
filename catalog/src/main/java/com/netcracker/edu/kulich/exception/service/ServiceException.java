package com.netcracker.edu.kulich.exception.service;

import com.netcracker.edu.kulich.exception.CatalogException;
import org.springframework.http.HttpStatus;

public class ServiceException extends CatalogException {
    public ServiceException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
