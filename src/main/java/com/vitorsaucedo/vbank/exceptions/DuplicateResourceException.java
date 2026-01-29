package com.vitorsaucedo.vbank.exceptions;

public class DuplicateResourceException extends VbankException {

    public DuplicateResourceException(String message) {
        super(message);
    }

    public DuplicateResourceException(String resourceName, String field) {
        super(String.format("%s já cadastrado(a) com este(a) %s.", resourceName, field));
    }
}
