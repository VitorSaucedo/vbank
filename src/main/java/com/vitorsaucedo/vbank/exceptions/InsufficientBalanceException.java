package com.vitorsaucedo.vbank.exceptions;

import java.math.BigDecimal;

public class InsufficientBalanceException extends VbankException {

    public InsufficientBalanceException() {
        super("Saldo insuficiente para realizar a operação.");
    }

    public InsufficientBalanceException(BigDecimal balance, BigDecimal required) {
        super(String.format("Saldo insuficiente. Disponível: R$ %.2f, Necessário: R$ %.2f",
                balance, required));
    }

    public InsufficientBalanceException(String message) {
        super(message);
    }
}
