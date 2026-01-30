package com.vitorsaucedo.vbank.dtos;

import com.vitorsaucedo.vbank.entities.enums.TransactionDirection;
import com.vitorsaucedo.vbank.entities.enums.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Item do extrato bancário")
public record StatementItemResponse(

        @Schema(description = "Identificador único da transação", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,

        @Schema(description = "Valor da transação", example = "150.50")
        BigDecimal amount,

        @Schema(description = "Data e hora da transação", example = "2025-01-29T10:15:30")
        LocalDateTime date,

        @Schema(description = "Descrição da transação", example = "Pagamento de aluguel")
        String description,

        @Schema(description = "Nome da outra parte envolvida na transação (quem enviou ou recebeu)",
                example = "Maria Souza")
        String otherPartyName,

        @Schema(
                description = "Tipo da transação",
                example = "PIX",
                implementation = TransactionType.class
        )
        TransactionType type,

        @Schema(
                description = "Direção da transação em relação ao usuário",
                example = "OUTBOUND",
                implementation = TransactionDirection.class
        )
        TransactionDirection direction
) {}
