package com.vitorsaucedo.vbank.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Dados consolidados do dashboard da conta bancária")
public record AccountDashboardResponse(

        @Schema(description = "Nome completo do titular da conta", example = "João da Silva Santos")
        String fullName,

        @Schema(description = "Número da conta bancária", example = "0001234567")
        String accountNumber,

        @Schema(description = "Número da agência", example = "0001")
        String agency,

        @Schema(description = "Saldo atual da conta em reais", example = "1250.75")
        BigDecimal balance
) {}
