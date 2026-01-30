package com.vitorsaucedo.vbank.exceptions;

import com.vitorsaucedo.vbank.config.GlobalExceptionHandler;

/**
 * Exceção lançada quando as credenciais de login são inválidas.
 *
 * @apiNote Retorna HTTP 401 (Unauthorized)
 * @see GlobalExceptionHandler#handleInvalidCredentialsException(Exception)
 */
public class InvalidCredentialsException extends VbankException {

    public InvalidCredentialsException() {
        super("Email ou senha incorretos.");
    }

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
