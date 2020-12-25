package com.rodrigocso.groceries.exception;

public class FieldValidationError {
    private final String field;
    private final String message;

    FieldValidationError(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }
}
