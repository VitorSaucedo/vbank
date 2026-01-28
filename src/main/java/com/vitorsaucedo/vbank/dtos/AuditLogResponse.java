package com.vitorsaucedo.vbank.dtos;

import java.time.LocalDateTime;

public record AuditLogResponse(
        String action,
        String details,
        LocalDateTime createdAt
) {}
