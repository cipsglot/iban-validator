package com.pfc.handler.domain;

public class ApiResponse {

    private boolean isValid;
    private String message;

    public ApiResponse(boolean isValid, String message){
        this.isValid = isValid;
        this.message = message;
    }
}
