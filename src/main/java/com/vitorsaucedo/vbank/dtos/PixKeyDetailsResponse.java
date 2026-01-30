package com.vitorsaucedo.vbank.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Detalhes do titular de uma chave PIX")
public record PixKeyDetailsResponse(

        @Schema(description = "Nome do titular (pode ser parcialmente mascarado para segurança)",
                example = "Jo** S****")
        String fullName,

        @Schema(description = "Documento mascarado do titular", example = "***456789**")
        String document,

        @Schema(description = "Nome do banco", example = "Vbank", defaultValue = "Vbank")
        String bankName,

        @Schema(description = "Número da conta bancária", example = "0001234567")
        String accountNumber,

        @Schema(description = "Número da agência", example = "0001")
        String agency
) {}
