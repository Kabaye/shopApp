package com.netcracker.edu.kulich.exception.service;

import com.netcracker.edu.kulich.exception.CatalogException;
import org.springframework.http.HttpStatus;

public class OfferServiceException extends CatalogException {
    public OfferServiceException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
