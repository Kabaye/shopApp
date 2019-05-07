package com.netcracker.edu.kulich.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class CatalogException extends RuntimeException {
    private HttpStatus status;

    public CatalogException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
