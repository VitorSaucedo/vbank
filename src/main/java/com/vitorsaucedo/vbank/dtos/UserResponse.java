package com.vitorsaucedo.vbank.dtos;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String fullName,
        String email,
        String accountNumber,
        String agency
) {}
