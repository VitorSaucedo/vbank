package com.vitorsaucedo.vbank.dtos;

import java.math.BigDecimal;

public record AccountDashboardResponse(
        String fullName,
        String accountNumber,
        String agency,
        BigDecimal balance
) {}
