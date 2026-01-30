package com.vitorsaucedo.vbank.exceptions;

import com.vitorsaucedo.vbank.config.GlobalExceptionHandler;

/**
 * Exceção de negócio genérica.
 *
 * @apiNote Retorna HTTP 400 (Bad Request)
 * @see GlobalExceptionHandler#handleBusinessException(VbankException)
 */
public class BusinessException extends VbankException {

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}