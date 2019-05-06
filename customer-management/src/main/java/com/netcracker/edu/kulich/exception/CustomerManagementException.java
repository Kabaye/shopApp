package com.netcracker.edu.kulich.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class CustomerManagementException extends RuntimeException {
    private HttpStatus status;

    public CustomerManagementException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
