package com.vitorsaucedo.vbank.services;

import com.vitorsaucedo.vbank.dtos.StatementItemResponse;
import com.vitorsaucedo.vbank.entities.BankAccount;
import com.vitorsaucedo.vbank.entities.Transaction;
import com.vitorsaucedo.vbank.mappers.TransactionMapper;
import com.vitorsaucedo.vbank.repositories.BankAccountRepository;
import com.vitorsaucedo.vbank.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final BankAccountRepository accountRepository;
    private final TransactionMapper transactionMapper; // Injeção do Mapper especializado

    /**
     * Recupera o extrato do usuário.
     * A lógica de "direção" da transação agora vive no Mapper.
     */
    @Transactional(readOnly = true)
    public List<StatementItemResponse> getStatement(UUID userId) {
        // 1. Busca a conta vinculada ao ID do usuário
        BankAccount account = accountRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Conta bancária não encontrada."));

        // 2. Busca as transações (ordenadas pela mais recente)
        List<Transaction> transactions = transactionRepository.findAllByAccountOrderByCreatedAtDesc(account);

        // 3. Mapeia as entidades para DTOs via Mapper
        return transactions.stream()
                .map(t -> transactionMapper.toStatementItem(t, account))
                .collect(Collectors.toList());
    }
}