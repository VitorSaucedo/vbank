package com.vitorsaucedo.vbank.entities.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Status de processamento de uma transação")
public enum TransactionStatus {

    @Schema(description = "Transação criada e aguardando processamento")
    PENDING,

    @Schema(description = "Transação processada e concluída com sucesso")
    COMPLETED,

    @Schema(description = "Transação falhou durante o processamento (pode ser revertida)")
    FAILED
}
