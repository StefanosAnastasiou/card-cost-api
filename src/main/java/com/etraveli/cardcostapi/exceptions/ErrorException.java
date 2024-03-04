package com.etraveli.cardcostapi.exceptions;


public class ErrorException extends RuntimeException{

    private final String errorMessage;
    public ErrorException(String message) {
        super(message);
        this.errorMessage = message;

    }
}
