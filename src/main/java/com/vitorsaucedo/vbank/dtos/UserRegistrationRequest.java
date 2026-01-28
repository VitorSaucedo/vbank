package com.vitorsaucedo.vbank.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRegistrationRequest(
        @NotBlank(message = "O nome completo é obrigatório")
        String fullName,

        @NotBlank(message = "O documento (CPF/CNPJ) é obrigatório")
        @Pattern(regexp = "\\d{11}|\\d{14}", message = "Documento deve ter 11 (CPF) ou 14 (CNPJ) dígitos numéricos")
        String document,

        @Email(message = "E-mail inválido")
        @NotBlank(message = "O e-mail é obrigatório")
        String email,

        @NotBlank(message = "A senha de acesso é obrigatória")
        @Size(min = 8, message = "A senha de acesso deve ter no mínimo 8 caracteres")
        String password,

        @NotBlank(message = "O PIN de transação é obrigatório")
        @Size(min = 4, max = 4, message = "O PIN de transação deve ter exatamente 4 dígitos")
        @Pattern(regexp = "\\d{4}", message = "O PIN deve conter apenas números")
        String transactionPin
) {}
