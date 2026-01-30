package com.vitorsaucedo.vbank.exceptions;

import com.vitorsaucedo.vbank.config.GlobalExceptionHandler;

/**
 * Exceção lançada quando uma operação é tentada em uma conta inativa.
 *
 * Contas inativas não podem:
 * - Realizar transferências
 * - Cadastrar chaves PIX
 * - Receber transferências
 *
 * @apiNote Retorna HTTP 403 (Forbidden)
 * @see GlobalExceptionHandler#handleInactiveAccountException(InactiveAccountException)
 */
public class InactiveAccountException extends VbankException {

    public InactiveAccountException() {
        super("A conta está inativa e não pode realizar transações.");
    }

    public InactiveAccountException(String message) {
        super(message);
    }
}
