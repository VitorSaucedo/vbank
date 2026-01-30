package com.vitorsaucedo.vbank.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Requisição de autenticação (login)")
public record LoginRequest(

        @Schema(description = "E-mail do usuário cadastrado", example = "joao.silva@email.com", required = true)
        @NotBlank
        String email,

        @Schema(description = "Senha de acesso", example = "minhasenha123", required = true, accessMode = Schema.AccessMode.WRITE_ONLY)
        @NotBlank
        String password
) {}
