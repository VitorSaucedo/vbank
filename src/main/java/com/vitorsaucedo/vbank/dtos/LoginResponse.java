package com.vitorsaucedo.vbank.dtos;

public record LoginResponse(
        String token,
        String type,
        Long expiresIn
) {}
