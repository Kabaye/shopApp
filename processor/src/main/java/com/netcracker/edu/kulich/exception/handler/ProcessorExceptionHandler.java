package com.netcracker.edu.kulich.exception.handler;


import com.netcracker.edu.kulich.exception.ProcessorException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ProcessorExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = ProcessorException.class)
    private ResponseEntity handleConflict(ProcessorException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(),
                new HttpHeaders(), ex.getStatus(), request);
    }
}
