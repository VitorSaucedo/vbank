package com.vitorsaucedo.vbank.exceptions;

import com.vitorsaucedo.vbank.config.GlobalExceptionHandler;

/**
 * Exceção lançada quando o PIN de transação está incorreto.
 *
 * @apiNote Retorna HTTP 401 (Unauthorized)
 * @see GlobalExceptionHandler#handleInvalidPinException(InvalidPinException)
 *
 * @implNote Em produção, considerar implementar bloqueio temporário após X tentativas
 */
public class InvalidPinException extends VbankException {

    public InvalidPinException() {
        super("PIN de transação incorreto. Verifique e tente novamente.");
    }

    public InvalidPinException(String message) {
        super(message);
    }
}
