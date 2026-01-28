package com.vitorsaucedo.vbank.services;

import com.vitorsaucedo.vbank.dtos.AccountDashboardResponse;
import com.vitorsaucedo.vbank.entities.BankAccount;
import com.vitorsaucedo.vbank.mappers.BankAccountMapper;
import com.vitorsaucedo.vbank.repositories.BankAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final BankAccountRepository accountRepository;
    private final BankAccountMapper bankAccountMapper; // Injeção do novo Mapper

    @Transactional(readOnly = true)
    public AccountDashboardResponse getDashboardData(UUID userId) {
        // 1. Recupera a entidade do banco de dados
        BankAccount account = accountRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Conta bancária não encontrada para o usuário informado."));

        // 2. Utiliza o mapper para transformar a Entidade em DTO
        return bankAccountMapper.toDashboardResponse(account);
    }
}