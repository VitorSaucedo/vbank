package com.vitorsaucedo.vbank.entities.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Status possíveis de uma conta bancária")
public enum AccountStatus {

    @Schema(description = "Conta ativa e operacional, pode realizar todas as operações")
    ACTIVE,

    @Schema(description = "Conta temporariamente bloqueada, não pode realizar transações")
    BLOCKED,

    @Schema(description = "Conta encerrada permanentemente, sem possibilidade de operações")
    CLOSED
}
