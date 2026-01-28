package com.vitorsaucedo.vbank.dtos;

import com.vitorsaucedo.vbank.entities.enums.PixKeyType;

import java.util.UUID;

public record PixKeyResponse(
        UUID id,
        PixKeyType keyType,
        String keyValue
) {}
