package com.vitorsaucedo.vbank.entities.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Tipos de transação bancária disponíveis no sistema")
public enum TransactionType {

    @Schema(description = "Transferência via PIX - instantânea e disponível 24/7")
    PIX,

    @Schema(description = "Transferência interna entre contas do mesmo banco (Vbank)")
    INTERNAL_TRANSFER,

    @Schema(description = "Depósito em conta - entrada de dinheiro")
    DEPOSIT
}
