package com.vitorsaucedo.vbank.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record PixTransferRequest(
        @NotBlank(message = "A chave Pix de destino é obrigatória")
        String targetKey,

        @NotNull(message = "O valor é obrigatório")
        @Positive(message = "O valor da transferência deve ser maior que zero")
        BigDecimal amount,

        @NotBlank(message = "O PIN de transação é necessário para autorizar")
        @Size(min = 4, max = 4)
        String transactionPin,

        @Size(max = 255, message = "A descrição pode ter no máximo 255 caracteres")
        String description
) {}
