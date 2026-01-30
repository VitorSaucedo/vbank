package com.vitorsaucedo.vbank.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Requisição para criar um log de auditoria")
public record AuditLogRequest(

        @Schema(description = "ID do usuário que realizou a ação", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID userId,

        @Schema(description = "Tipo de ação realizada",
                example = "LOGIN_FAILED",
                allowableValues = {"LOGIN_SUCCESS", "LOGIN_FAILED", "TRANSACTION_PIN_CHANGED", "ACCOUNT_BLOCKED", "PIX_TRANSFER", "PIX_KEY_CREATED"})
        String action,

        @Schema(description = "Detalhes técnicos da ação (IP, User-Agent, erros, etc.)",
                example = "IP: 192.168.1.1, User-Agent: Mozilla/5.0")
        String details
) {}
