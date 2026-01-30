package com.vitorsaucedo.vbank.exceptions;

import com.vitorsaucedo.vbank.config.GlobalExceptionHandler;

/**
 * Classe base para todas as exceções personalizadas do Vbank.
 *
 * Todas as exceções de negócio devem herdar desta classe para
 * garantir tratamento consistente pelo {@link GlobalExceptionHandler}.
 *
 * @see BusinessException
 * @see DuplicateResourceException
 * @see InactiveAccountException
 * @see InsufficientBalanceException
 * @see InvalidCredentialsException
 * @see InvalidDataException
 * @see InvalidPinException
 * @see ResourceNotFoundException
 */
public abstract class VbankException extends RuntimeException {

    public VbankException(String message) {
        super(message);
    }

    public VbankException(String message, Throwable cause) {
        super(message, cause);
    }
}
