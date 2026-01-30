package com.vitorsaucedo.vbank.exceptions;

import com.vitorsaucedo.vbank.config.GlobalExceptionHandler;

import java.math.BigDecimal;

/**
 * Exceção lançada quando não há saldo suficiente para realizar uma operação.
 *
 * @apiNote Retorna HTTP 422 (Unprocessable Entity)
 * @see GlobalExceptionHandler#handleInsufficientBalanceException(InsufficientBalanceException)
 */
public class InsufficientBalanceException extends VbankException {

    public InsufficientBalanceException() {
        super("Saldo insuficiente para realizar a operação.");
    }

    /**
     * Construtor com detalhes de saldo disponível e necessário.
     *
     * @param balance Saldo disponível na conta
     * @param required Saldo necessário para a operação
     */
    public InsufficientBalanceException(BigDecimal balance, BigDecimal required) {
        super(String.format("Saldo insuficiente. Disponível: R$ %.2f, Necessário: R$ %.2f",
                balance, required));
    }

    public InsufficientBalanceException(String message) {
        super(message);
    }
}
