package com.vitorsaucedo.vbank.exceptions;

public class BusinessException extends VbankException{

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}