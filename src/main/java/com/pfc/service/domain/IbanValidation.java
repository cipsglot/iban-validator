package com.pfc.service.domain;

public class IbanValidation {

    private boolean valid;
    private boolean validationError;
    private String message;

    public IbanValidation(boolean valid, boolean validationError, String message) {
        this.valid = valid;
        this.validationError = validationError;
        this.message = message;
    }

    public boolean isValid() {
        return valid;
    }

    public boolean isValidationError() {
        return validationError;
    }

    public String getMessage() {
        return message;
    }
}
