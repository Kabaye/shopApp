package com.netcracker.edu.kulich.exception.handler;

import com.netcracker.edu.kulich.exception.CatalogException;
import com.netcracker.edu.kulich.logging.annotation.DefaultLogging;
import com.netcracker.edu.kulich.logging.annotation.Logging;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@DefaultLogging
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = CatalogException.class)
    @Logging(startMessage = "Handling global controller exceptions...", endMessage = "Global controller exception handled.", level = LogLevel.WARN)
    public ResponseEntity handleConflict(CatalogException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(),
                new HttpHeaders(), ex.getStatus(), request);
    }

    @ExceptionHandler(value = ClassCastException.class)
    @Logging(startMessage = "Handling global controller exceptions...", endMessage = "Global controller exception handled.", level = LogLevel.WARN)
    public ResponseEntity handleCastConflict(ClassCastException ex, WebRequest request) {
        return handleExceptionInternal(ex, "You need to send 2 prices only as one object.",
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(RuntimeException.class)
    @Logging(startMessage = "Handling some unknown exception...", endMessage = "Unknown exception handled.", level = LogLevel.WARN)
    public ResponseEntity handleUnknownConflict(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, "Unknown exception thrown. Check log for better understanding.",
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
}
