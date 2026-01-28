package com.vitorsaucedo.vbank.services;

import com.vitorsaucedo.vbank.dtos.*;
import com.vitorsaucedo.vbank.entities.*;
import com.vitorsaucedo.vbank.entities.enums.AccountStatus;
import com.vitorsaucedo.vbank.entities.enums.TransactionStatus;
import com.vitorsaucedo.vbank.entities.enums.TransactionType;
import com.vitorsaucedo.vbank.mappers.PixKeyMapper;
import com.vitorsaucedo.vbank.mappers.TransactionMapper;
import com.vitorsaucedo.vbank.repositories.BankAccountRepository;
import com.vitorsaucedo.vbank.repositories.PixKeyRepository;
import com.vitorsaucedo.vbank.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final BankAccountRepository accountRepository;
    private final PixKeyRepository pixKeyRepository;
    private final TransactionRepository transactionRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditLogService auditLogService;

    // Injeção dos Mappers para limpeza do serviço
    private final TransactionMapper transactionMapper;
    private final PixKeyMapper pixKeyMapper;

    /**
     * Busca detalhes do recebedor. A lógica de mascaramento (LGPD) agora vive no PixKeyMapper.
     */
    @Transactional(readOnly = true)
    public PixKeyDetailsResponse findReceiverByPixKey(String key) {
        PixKey pixKey = pixKeyRepository.findByKeyValue(key)
                .orElseThrow(() -> new RuntimeException("Chave Pix não encontrada."));

        return pixKeyMapper.toDetailsResponse(pixKey);
    }

    /**
     * Executa a transferência Pix de forma atômica e limpa.
     */
    @Transactional
    public TransactionResponse executePix(PixTransferRequest request, UUID userId) {
        // 1. Busca conta do pagador
        BankAccount payerAccount = accountRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Conta do pagador não encontrada."));

        // 2. Validações de segurança e status
        validateTransfer(request, payerAccount);

        // 3. Busca recebedor
        PixKey targetKey = pixKeyRepository.findByKeyValue(request.targetKey())
                .orElseThrow(() -> new RuntimeException("Chave Pix de destino não encontrada."));
        BankAccount payeeAccount = targetKey.getAccount();

        // 4. Executa movimentação financeira
        payerAccount.withdraw(request.amount());
        payeeAccount.deposit(request.amount());

        // 5. Criação e persistência da transação
        Transaction savedTransaction = transactionRepository.save(
                transactionMapper.toEntity(request, payerAccount, payeeAccount)
        );

        accountRepository.save(payerAccount);
        accountRepository.save(payeeAccount);

        // 6. Auditoria
        auditLogService.log(new AuditLogRequest(
                userId,
                "PIX_SENT",
                "Pix de R$ " + request.amount() + " para chave: " + request.targetKey()
        ));

        // 7. Retorna o comprovante via Mapper
        return transactionMapper.toResponse(savedTransaction);
    }

    private void validateTransfer(PixTransferRequest request, BankAccount payerAccount) {
        if (!passwordEncoder.matches(request.transactionPin(), payerAccount.getUser().getTransactionPin())) {
            auditLogService.log(new AuditLogRequest(payerAccount.getUser().getId(), "INVALID_PIX_PIN", "PIN incorreto"));
            throw new RuntimeException("PIN de transação inválido.");
        }

        if (payerAccount.getStatus() != AccountStatus.ACTIVE) {
            throw new RuntimeException("A conta de origem não está ativa.");
        }
    }
}