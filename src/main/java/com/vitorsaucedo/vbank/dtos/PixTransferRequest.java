package com.vitorsaucedo.vbank.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Schema(description = "Requisição para realizar transferência PIX")
public record PixTransferRequest(

        @Schema(description = "Chave PIX do destinatário (pode ser CPF, CNPJ, e-mail, telefone ou chave aleatória)",
                example = "destinatario@email.com",
                required = true)
        @NotBlank(message = "A chave Pix de destino é obrigatória")
        String targetKey,

        @Schema(description = "Valor da transferência em reais",
                example = "150.50",
                required = true,
                minimum = "0.01")
        @NotNull(message = "O valor é obrigatório")
        @Positive(message = "O valor da transferência deve ser maior que zero")
        BigDecimal amount,

        @Schema(description = "PIN de transação para autorizar a operação",
                example = "1234",
                required = true,
                minLength = 4,
                maxLength = 4,
                accessMode = Schema.AccessMode.WRITE_ONLY)
        @NotBlank(message = "O PIN de transação é necessário para autorizar")
        @Size(min = 4, max = 4)
        String transactionPin,

        @Schema(description = "Descrição ou observação sobre a transferência",
                example = "Pagamento de aluguel - Janeiro/2025",
                maxLength = 255,
                nullable = true)
        @Size(max = 255, message = "A descrição pode ter no máximo 255 caracteres")
        String description
) {}
