package com.netcracker.edu.kulich.exception.controller;

import com.netcracker.edu.kulich.exception.CatalogException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class OfferControllerException extends CatalogException {
    public OfferControllerException(String message) {
        super(message, NOT_FOUND);
    }
}
