package com.vitorsaucedo.vbank.exceptions;

public class InvalidDataException extends VbankException{

    public InvalidDataException(String message) {
        super(message);
    }

    public InvalidDataException(String fieldName, String reason) {
        super(String.format("Dados inválidos no campo '%s': %s", fieldName, reason));
    }
}
