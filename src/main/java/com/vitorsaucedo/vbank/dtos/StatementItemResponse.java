package com.vitorsaucedo.vbank.dtos;

import com.vitorsaucedo.vbank.entities.enums.TransactionDirection;
import com.vitorsaucedo.vbank.entities.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record StatementItemResponse(
        UUID id,
        BigDecimal amount,
        LocalDateTime date,
        String description,
        String otherPartyName, // Nome de quem enviou ou recebeu
        TransactionType type,
        TransactionDirection direction // INBOUND ou OUTBOUND
) {}
