package com.vitorsaucedo.vbank.exceptions;

public class InactiveAccountException extends VbankException {
    public InactiveAccountException() {
        super("A conta está inativa e não pode realizar transações.");
    }

    public InactiveAccountException(String message) {
        super(message);
    }
}
