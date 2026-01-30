package com.vitorsaucedo.vbank.entities.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Direção de uma transação em relação ao usuário")
public enum TransactionDirection {

    @Schema(description = "Transação recebida - dinheiro entrando na conta do usuário")
    INBOUND,

    @Schema(description = "Transação enviada - dinheiro saindo da conta do usuário")
    OUTBOUND
}
