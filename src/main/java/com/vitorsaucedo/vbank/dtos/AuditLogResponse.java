package com.vitorsaucedo.vbank.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Resposta com dados de um log de auditoria")
public record AuditLogResponse(

        @Schema(description = "Tipo de ação realizada", example = "LOGIN_FAILED")
        String action,

        @Schema(description = "Detalhes técnicos da ação", example = "IP: 192.168.1.1, User-Agent: Mozilla/5.0")
        String details,

        @Schema(description = "Data e hora em que a ação foi registrada", example = "2025-01-29T10:15:30")
        LocalDateTime createdAt
) {}
