package com.netcracker.edu.kulich.exception.remote;

import com.netcracker.edu.kulich.exception.ProcessorException;
import org.springframework.http.HttpStatus;

public class RemoteAPIException extends ProcessorException {
    public RemoteAPIException(String message, HttpStatus status) {
        super("Exception from remote API: \"" + message + "\";", status);
    }
}
