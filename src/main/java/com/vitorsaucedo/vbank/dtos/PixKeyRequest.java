package com.vitorsaucedo.vbank.dtos;

import com.vitorsaucedo.vbank.entities.enums.PixKeyType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Requisição para cadastrar uma nova chave PIX")
public record PixKeyRequest(

        @Schema(
                description = "Tipo da chave PIX a ser cadastrada",
                example = "EMAIL",
                required = true,
                implementation = PixKeyType.class
        )
        @NotNull(message = "O tipo da chave é obrigatório")
        PixKeyType keyType,

        @Schema(
                description = """
                Valor da chave PIX de acordo com o tipo selecionado:
                - CPF: 11 dígitos numéricos (ex: 12345678900)
                - CNPJ: 14 dígitos numéricos (ex: 12345678000190)
                - EMAIL: endereço de e-mail válido (ex: usuario@email.com)
                - PHONE: telefone com DDD, 11 dígitos (ex: 11987654321)
                - RANDOM: deixar null ou vazio (será gerado automaticamente)
                """,
                example = "joao.silva@email.com",
                nullable = true
        )
        String keyValue
) {}
