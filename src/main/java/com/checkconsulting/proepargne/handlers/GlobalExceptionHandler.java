package com.checkconsulting.proepargne.handlers;

import com.checkconsulting.proepargne.exception.GlobalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseTemplate> handleValidationErrors(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        log.info("Exception message: {}", ex.getMessage());
        log.info("Exception stackTrace: {}", ex.getStackTrace());

        ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));

        return new ResponseEntity<ResponseTemplate>(ResponseTemplate
                .builder()
                .errorMessage(ex.getMessage())
                .validations(errors)
                .build(), HttpStatus.BAD_REQUEST
        );
    }


    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ResponseTemplate> handleException(GlobalException ex) {

        log.error("Exception message: {}", ex.getMessage());
        log.error("Exception stackTrace: {}", ex.getStackTrace());

        return new ResponseEntity<>(ResponseTemplate
                .builder()
                .errorMessage(ex.getMessage())
                .validations(null)
                .build(), ex.getStatus()
        );
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseTemplate> handleException(Exception ex) {

        log.error("Validation message: {}", ex.getMessage());
        log.error("Validation stackTrace: {}", ex.getStackTrace());

        return new ResponseEntity<>(ResponseTemplate
                .builder()
                .errorMessage(ex.getMessage())
                .validations(null)
                .build(), HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

}