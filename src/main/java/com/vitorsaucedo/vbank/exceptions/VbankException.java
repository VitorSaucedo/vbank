package com.vitorsaucedo.vbank.exceptions;

public abstract class VbankException extends RuntimeException {

    public VbankException(String message) {
        super(message);
    }

    public VbankException(String message, Throwable cause) {
        super(message, cause);
    }
}
