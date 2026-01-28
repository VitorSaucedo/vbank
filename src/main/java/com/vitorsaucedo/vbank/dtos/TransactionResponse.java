package com.vitorsaucedo.vbank.dtos;

import com.vitorsaucedo.vbank.entities.enums.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionResponse(
        UUID transactionId,
        BigDecimal amount,
        LocalDateTime timestamp,
        String description,
        TransactionStatus status,
        AccountInfo payer,
        AccountInfo payee
) {
    public record AccountInfo(
            String name,
            String document,
            String bank,
            String agency,
            String account
    ) {}
}
