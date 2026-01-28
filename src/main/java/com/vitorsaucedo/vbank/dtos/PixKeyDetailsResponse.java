package com.vitorsaucedo.vbank.dtos;

public record PixKeyDetailsResponse(
        String fullName,      // Nome (pode ser mascarado: Jo** S****)
        String document,      // CPF mascarado
        String bankName,      // "NexusBank"
        String accountNumber,
        String agency
) {}
