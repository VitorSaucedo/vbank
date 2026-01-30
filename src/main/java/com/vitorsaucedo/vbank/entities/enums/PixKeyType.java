package com.vitorsaucedo.vbank.entities.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Tipos de chave PIX disponíveis no sistema brasileiro")
public enum PixKeyType {

    @Schema(description = "Chave PIX tipo CPF - 11 dígitos numéricos (ex: 12345678900)")
    CPF,

    @Schema(description = "Chave PIX tipo CNPJ - 14 dígitos numéricos (ex: 12345678000190)")
    CNPJ,

    @Schema(description = "Chave PIX tipo e-mail (ex: usuario@email.com)")
    EMAIL,

    @Schema(description = "Chave PIX tipo telefone celular com DDD (ex: 11987654321)")
    PHONE,

    @Schema(description = "Chave PIX aleatória gerada automaticamente pelo sistema (UUID)")
    RANDOM
}
