package com.vitorsaucedo.vbank.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Resposta com dados do usuário registrado")
public record UserResponse(

        @Schema(description = "Identificador único do usuário", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,

        @Schema(description = "Nome completo do usuário", example = "João da Silva Santos")
        String fullName,

        @Schema(description = "E-mail do usuário", example = "joao.silva@email.com")
        String email,

        @Schema(description = "Número da conta bancária", example = "0001234567")
        String accountNumber,

        @Schema(description = "Número da agência", example = "0001")
        String agency
) {}
