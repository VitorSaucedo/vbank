package com.vitorsaucedo.vbank.exceptions;

public class ResourceNotFoundException extends VbankException {

    public ResourceNotFoundException(String resourceName) {
        super(resourceName + " não encontrado(a).");
    }

    public ResourceNotFoundException(String resourceName, String identifier) {
        super(String.format("%s não encontrado(a) com identificador: %s", resourceName, identifier));
    }
}
