package com.pfc.service.domain;

public class IbanValidation {

    private boolean valid;
    private String message;

    public IbanValidation(boolean valid, String message) {
        this.valid = valid;
        this.message = message;
    }

    public boolean isValid() {
        return valid;
    }

    public String getMessage() {
        return message;
    }
}
