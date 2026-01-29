package com.vitorsaucedo.vbank.dtos;

import com.vitorsaucedo.vbank.entities.enums.PixKeyType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PixKeyRequest(
        @NotNull(message = "O tipo da chave é obrigatório")
        PixKeyType keyType,

        String keyValue
) {}
