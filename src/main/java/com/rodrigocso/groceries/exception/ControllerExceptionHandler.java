package com.rodrigocso.groceries.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ErrorResponse handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        ErrorResponse errorResponse = new ErrorResponse();

        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errorResponse.getFieldErrors().add(
                    new FieldValidationError(fieldError.getField(), fieldError.getDefaultMessage()));
        }

        return errorResponse;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    Map<String, String> handleDataIntegrityViolation(DataIntegrityViolationException e) {
        Map<String, String> body = new HashMap<>();
        body.put("error", e.getMessage());

        return body;
    }
}
