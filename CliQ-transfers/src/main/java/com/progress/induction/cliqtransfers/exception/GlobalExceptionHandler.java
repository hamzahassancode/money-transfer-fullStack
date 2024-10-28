package com.progress.induction.cliqtransfers.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorModel handleNotFoundException(NotFoundException ex) {
        ErrorModel error = new ErrorModel();
        error.setMessage(ex.getMessage());
        log.warn("NotFoundException occurred: {}", ex.getMessage(), ex);
        return error;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorModel handleApplicationException(ApplicationException ex) {
        ErrorModel error = new ErrorModel();
        error.setMessage(ex.getMessage());
        log.warn("Bad request occurred: {}", ex.getMessage(), ex);
        return error;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorModel handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        ErrorModel error = new ErrorModel();
        error.setMessage(ex.getMessage());
        log.warn("Bad request occurred: {}", ex.getMessage(), ex);
        return error;
    }
}

//HttpMessageNotReadableException
