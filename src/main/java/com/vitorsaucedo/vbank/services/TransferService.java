package com.vitorsaucedo.vbank.services;

import com.vitorsaucedo.vbank.dtos.AuditLogRequest;
import com.vitorsaucedo.vbank.dtos.PixKeyDetailsResponse;
import com.vitorsaucedo.vbank.dtos.PixTransferRequest;
import com.vitorsaucedo.vbank.dtos.TransactionResponse;
import com.vitorsaucedo.vbank.entities.BankAccount;
import com.vitorsaucedo.vbank.entities.PixKey;
import com.vitorsaucedo.vbank.entities.Transaction;
import com.vitorsaucedo.vbank.entities.enums.AccountStatus;
import com.vitorsaucedo.vbank.exceptions.InactiveAccountException;
import com.vitorsaucedo.vbank.exceptions.InvalidDataException;
import com.vitorsaucedo.vbank.exceptions.InvalidPinException;
import com.vitorsaucedo.vbank.exceptions.ResourceNotFoundException;
import com.vitorsaucedo.vbank.mappers.PixKeyMapper;
import com.vitorsaucedo.vbank.mappers.TransactionMapper;
import com.vitorsaucedo.vbank.repositories.BankAccountRepository;
import com.vitorsaucedo.vbank.repositories.PixKeyRepository;
import com.vitorsaucedo.vbank.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final BankAccountRepository accountRepository;
    private final PixKeyRepository pixKeyRepository;
    private final TransactionRepository transactionRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditLogService auditLogService;

    private final TransactionMapper transactionMapper;
    private final PixKeyMapper pixKeyMapper;

    @Transactional(readOnly = true)
    public PixKeyDetailsResponse findReceiverByPixKey(String key) {
        if (key == null || key.isBlank()) {
            throw new InvalidDataException("key", "Chave PIX não pode estar vazia.");
        }

        PixKey pixKey = pixKeyRepository.findByKeyValue(key)
                .orElseThrow(() -> new ResourceNotFoundException("Chave Pix", key));

        return pixKeyMapper.toDetailsResponse(pixKey);
    }

    @Transactional
    public TransactionResponse executePix(PixTransferRequest request, UUID userId) {
        validateTransferRequest(request);

        BankAccount payerAccount = accountRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Conta bancária", userId.toString()));

        validateTransfer(request, payerAccount);

        PixKey targetKey = pixKeyRepository.findByKeyValue(request.targetKey())
                .orElseThrow(() -> new ResourceNotFoundException("Chave Pix", request.targetKey()));
        BankAccount payeeAccount = targetKey.getAccount();

        if (payerAccount.getId().equals(payeeAccount.getId())) {
            throw new InvalidDataException(
                    "targetKey",
                    "Não é possível transferir para sua própria conta."
            );
        }

        if (payeeAccount.getStatus() != AccountStatus.ACTIVE) {
            throw new InvalidDataException(
                    "targetKey",
                    "A conta de destino está inativa e não pode receber transferências."
            );
        }

        payerAccount.withdraw(request.amount());
        payeeAccount.deposit(request.amount());

        Transaction savedTransaction = transactionRepository.save(
                transactionMapper.toEntity(request, payerAccount, payeeAccount)
        );

        accountRepository.save(payerAccount);
        accountRepository.save(payeeAccount);

        auditLogService.log(new AuditLogRequest(
                userId,
                "PIX_SENT",
                String.format("Pix de R$ %.2f para chave: %s (Conta: %s)",
                        request.amount(),
                        request.targetKey(),
                        payeeAccount.getAccountNumber())
        ));

        return transactionMapper.toResponse(savedTransaction);
    }

    private void validateTransferRequest(PixTransferRequest request) {
        // Validação de chave PIX
        if (request.targetKey() == null || request.targetKey().isBlank()) {
            throw new InvalidDataException(
                    "targetKey",
                    "Chave PIX de destino é obrigatória."
            );
        }

        if (request.amount() == null || request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidDataException(
                    "amount",
                    "O valor da transferência deve ser maior que zero."
            );
        }

        BigDecimal maxAmount = new BigDecimal("50000.00");
        if (request.amount().compareTo(maxAmount) > 0) {
            throw new InvalidDataException(
                    "amount",
                    String.format("O valor máximo por transação é R$ %.2f", maxAmount)
            );
        }

        if (request.transactionPin() == null || request.transactionPin().isBlank()) {
            throw new InvalidDataException(
                    "transactionPin",
                    "PIN de transação é obrigatório."
            );
        }

        if (!request.transactionPin().matches("\\d{4}")) {
            throw new InvalidDataException(
                    "transactionPin",
                    "PIN de transação inválido. Deve conter 4 dígitos."
            );
        }
    }

    private void validateTransfer(PixTransferRequest request, BankAccount payerAccount) {
        if (!passwordEncoder.matches(request.transactionPin(), payerAccount.getUser().getTransactionPin())) {
            auditLogService.log(new AuditLogRequest(
                    payerAccount.getUser().getId(),
                    "INVALID_PIX_PIN",
                    "Tentativa de PIX com PIN incorreto"
            ));
            throw new InvalidPinException();
        }

        if (payerAccount.getStatus() != AccountStatus.ACTIVE) {
            throw new InactiveAccountException(
                    "Sua conta está " + payerAccount.getStatus().name().toLowerCase() +
                            " e não pode realizar transferências."
            );
        }
    }
}