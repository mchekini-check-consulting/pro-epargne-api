package com.checkconsulting.proepargne.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseTemplate> handleValidationErrors(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));

        return new ResponseEntity<ResponseTemplate>(ResponseTemplate
                .builder()
                .error(ex.getStatusCode().toString())
                .validations(errors)
                .build(), HttpStatus.BAD_REQUEST
        );
    }


    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ResponseTemplate> handleException(GlobalException ex) {

        return new ResponseEntity<>(ResponseTemplate
                .builder()
                .errorMessage(ex.getMessage())
                .validations(null)
                .build(), ex.getStatus()
        );
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseTemplate> handleException(Exception ex) {

        return new ResponseEntity<>(ResponseTemplate
                .builder()
                .errorMessage(ex.getMessage())
                .validations(null)
                .build(), HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

}