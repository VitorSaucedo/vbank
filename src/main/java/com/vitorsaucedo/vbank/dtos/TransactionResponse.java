package com.vitorsaucedo.vbank.dtos;

import com.vitorsaucedo.vbank.entities.enums.TransactionStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Resposta detalhada de uma transação concluída")
public record TransactionResponse(

        @Schema(description = "Identificador único da transação", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID transactionId,

        @Schema(description = "Valor da transação em reais", example = "150.50", minimum = "0.01")
        BigDecimal amount,

        @Schema(description = "Data e hora em que a transação foi processada", example = "2025-01-29T10:15:30")
        LocalDateTime timestamp,

        @Schema(description = "Descrição ou observação da transação", example = "Pagamento de aluguel", nullable = true)
        String description,

        @Schema(
                description = "Status de processamento da transação",
                example = "COMPLETED",
                implementation = TransactionStatus.class
        )
        TransactionStatus status,

        @Schema(description = "Informações completas do pagador (quem enviou o dinheiro)")
        AccountInfo payer,

        @Schema(description = "Informações completas do beneficiário (quem recebeu o dinheiro)")
        AccountInfo payee
) {
    @Schema(description = "Informações detalhadas de uma conta bancária envolvida na transação")
    public record AccountInfo(

            @Schema(description = "Nome completo do titular da conta", example = "João da Silva Santos")
            String name,

            @Schema(description = "Documento do titular (CPF ou CNPJ)", example = "12345678900")
            String document,

            @Schema(description = "Nome da instituição bancária", example = "Vbank", defaultValue = "Vbank")
            String bank,

            @Schema(description = "Número da agência bancária", example = "0001")
            String agency,

            @Schema(description = "Número da conta bancária", example = "0001234567")
            String account
    ) {}
}
