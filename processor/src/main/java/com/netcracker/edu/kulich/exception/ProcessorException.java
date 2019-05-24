package com.netcracker.edu.kulich.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class ProcessorException extends RuntimeException {
    private HttpStatus status;

    public ProcessorException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
