package com.rodrigocso.groceries.exception;

import java.util.ArrayList;
import java.util.List;

public class ErrorResponse {
    private final List<FieldValidationError> fieldErrors = new ArrayList<>();

    public List<FieldValidationError> getFieldErrors() {
        return fieldErrors;
    }
}
