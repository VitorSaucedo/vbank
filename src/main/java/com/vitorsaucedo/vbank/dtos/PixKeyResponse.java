package com.vitorsaucedo.vbank.dtos;

import com.vitorsaucedo.vbank.entities.enums.PixKeyType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Resposta com dados de uma chave PIX cadastrada")
public record PixKeyResponse(

        @Schema(description = "Identificador único da chave PIX", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,

        @Schema(
                description = "Tipo da chave PIX",
                example = "EMAIL",
                implementation = PixKeyType.class
        )
        PixKeyType keyType,

        @Schema(description = "Valor da chave PIX", example = "joao.silva@email.com")
        String keyValue
) {}
