package com.vitorsaucedo.vbank.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta de autenticação com token JWT")
public record LoginResponse(

        @Schema(description = "Token JWT para autenticação nas requisições",
                example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String token,

        @Schema(description = "Tipo do token de autenticação", example = "Bearer", defaultValue = "Bearer")
        String type,

        @Schema(description = "Tempo de expiração do token em segundos", example = "3600")
        Long expiresIn
) {}
