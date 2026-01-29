package com.vitorsaucedo.vbank.exceptions;

public class InvalidPinException extends VbankException {

    public InvalidPinException() {
        super("PIN de transação incorreto. Verifique e tente novamente.");
    }

    public InvalidPinException(String message) {
        super(message);
    }
}
