package com.vitorsaucedo.vbank.dtos;

import java.util.UUID;

public record AuditLogRequest(
        UUID userId,
        String action,
        String details
) {}
