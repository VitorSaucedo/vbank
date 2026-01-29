package com.vitorsaucedo.vbank.exceptions;

public class InvalidCredentialsException extends VbankException {

    public InvalidCredentialsException() {
        super("Email ou senha incorretos.");
    }

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
