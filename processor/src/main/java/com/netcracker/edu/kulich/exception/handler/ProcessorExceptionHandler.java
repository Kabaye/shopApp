package com.netcracker.edu.kulich.exception.handler;


import com.netcracker.edu.kulich.exception.ProcessorException;
import com.netcracker.edu.kulich.logging.annotation.DefaultLogging;
import com.netcracker.edu.kulich.logging.annotation.Logging;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@DefaultLogging
@ControllerAdvice
public class ProcessorExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = ProcessorException.class)
    @Logging(startMessage = "Handling global controller exceptions...", endMessage = "Global controller exception handled.", level = LogLevel.WARN)
    public ResponseEntity handleConflict(ProcessorException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(),
                new HttpHeaders(), ex.getStatus(), request);
    }

    @ExceptionHandler(RuntimeException.class)
    @Logging(startMessage = "Handling some unknown exception...", endMessage = "Unknown exception handled.", level = LogLevel.WARN)
    public ResponseEntity handleUnknownConflict(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, "Unknown exception thrown. Check log for better understanding.",
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
}
