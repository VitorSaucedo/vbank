package com.vitorsaucedo.vbank.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Requisição para registro de novo usuário no sistema")
public record UserRegistrationRequest(

        @Schema(description = "Nome completo do usuário", example = "João da Silva Santos", required = true)
        @NotBlank(message = "O nome completo é obrigatório")
        String fullName,

        @Schema(description = "Documento de identificação (CPF com 11 dígitos ou CNPJ com 14 dígitos)",
                example = "12345678900",
                required = true,
                pattern = "\\d{11}|\\d{14}")
        @NotBlank(message = "O documento (CPF/CNPJ) é obrigatório")
        @Pattern(regexp = "\\d{11}|\\d{14}", message = "Documento deve ter 11 (CPF) ou 14 (CNPJ) dígitos numéricos")
        String document,

        @Schema(description = "Endereço de e-mail do usuário", example = "joao.silva@email.com", required = true)
        @Email(message = "E-mail inválido")
        @NotBlank(message = "O e-mail é obrigatório")
        String email,

        @Schema(description = "Senha de acesso à conta",
                example = "minhasenha123",
                required = true,
                minLength = 8,
                accessMode = Schema.AccessMode.WRITE_ONLY)
        @NotBlank(message = "A senha de acesso é obrigatória")
        @Size(min = 8, message = "A senha de acesso deve ter no mínimo 8 caracteres")
        String password,

        @Schema(description = "PIN numérico de 4 dígitos para autorizar transações",
                example = "1234",
                required = true,
                pattern = "\\d{4}",
                minLength = 4,
                maxLength = 4,
                accessMode = Schema.AccessMode.WRITE_ONLY)
        @NotBlank(message = "O PIN de transação é obrigatório")
        @Size(min = 4, max = 4, message = "O PIN de transação deve ter exatamente 4 dígitos")
        @Pattern(regexp = "\\d{4}", message = "O PIN deve conter apenas números")
        String transactionPin
) {}
