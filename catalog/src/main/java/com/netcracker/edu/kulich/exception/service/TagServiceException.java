package com.netcracker.edu.kulich.exception.service;

import com.netcracker.edu.kulich.exception.CatalogException;
import org.springframework.http.HttpStatus;

public class TagServiceException extends CatalogException {
    public TagServiceException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
