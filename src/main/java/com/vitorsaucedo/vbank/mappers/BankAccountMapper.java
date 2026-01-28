package com.vitorsaucedo.vbank.mappers;

import com.vitorsaucedo.vbank.dtos.AccountDashboardResponse;
import com.vitorsaucedo.vbank.entities.BankAccount;
import org.springframework.stereotype.Component;

@Component
public class BankAccountMapper {

    public AccountDashboardResponse toDashboardResponse(BankAccount account) {
        return new AccountDashboardResponse(
                account.getUser().getFullName(),
                account.getAccountNumber(),
                account.getAgency(),
                account.getBalance()
        );
    }
}
