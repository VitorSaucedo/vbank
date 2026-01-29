package com.vitorsaucedo.vbank.services;

import com.vitorsaucedo.vbank.dtos.AuditLogRequest;
import com.vitorsaucedo.vbank.dtos.PixKeyRequest;
import com.vitorsaucedo.vbank.dtos.PixKeyResponse;
import com.vitorsaucedo.vbank.entities.BankAccount;
import com.vitorsaucedo.vbank.entities.PixKey;
import com.vitorsaucedo.vbank.entities.enums.PixKeyType;
import com.vitorsaucedo.vbank.exceptions.DuplicateResourceException;
import com.vitorsaucedo.vbank.exceptions.InvalidDataException;
import com.vitorsaucedo.vbank.exceptions.ResourceNotFoundException;
import com.vitorsaucedo.vbank.mappers.PixKeyMapper;
import com.vitorsaucedo.vbank.repositories.BankAccountRepository;
import com.vitorsaucedo.vbank.repositories.PixKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PixKeyService {

    private final PixKeyRepository pixKeyRepository;
    private final BankAccountRepository accountRepository;
    private final AuditLogService auditLogService;
    private final PixKeyMapper pixKeyMapper;

    @Transactional
    public PixKeyResponse createKey(PixKeyRequest request, UUID userId) {
        BankAccount account = accountRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Conta bancária", userId.toString()));

        String keyValue = determineKeyValue(request.keyType(), request.keyValue(), account);

        if (pixKeyRepository.existsByKeyValue(keyValue)) {
            throw new DuplicateResourceException("Esta chave Pix já está em uso por outra conta.");
        }

        PixKey pixKey = new PixKey();
        pixKey.setAccount(account);
        pixKey.setKeyType(request.keyType());
        pixKey.setKeyValue(keyValue);

        PixKey savedKey = pixKeyRepository.save(pixKey);

        auditLogService.log(new AuditLogRequest(
                userId,
                "PIX_KEY_CREATED",
                "Nova chave do tipo " + request.keyType() + " cadastrada: " + keyValue
        ));

        return pixKeyMapper.toResponse(savedKey);
    }

    private String determineKeyValue(PixKeyType keyType, String requestValue, BankAccount account) {
        return switch (keyType) {
            case CPF, CNPJ -> {
                String document = account.getUser().getDocument();
                if (document == null || document.isBlank()) {
                    throw new InvalidDataException(
                            "document",
                            "Documento não cadastrado. Atualize seus dados antes de criar chave PIX."
                    );
                }
                yield document.replaceAll("\\D", ""); // Remove formatação
            }

            case EMAIL -> {
                String email = account.getUser().getEmail();
                if (email == null || email.isBlank()) {
                    throw new InvalidDataException(
                            "email",
                            "Email não cadastrado. Atualize seus dados antes de criar chave PIX."
                    );
                }
                yield email.toLowerCase().trim();
            }

            case RANDOM -> UUID.randomUUID().toString();

            case PHONE -> {
                if (requestValue == null || requestValue.isBlank()) {
                    throw new InvalidDataException(
                            "keyValue",
                            "O número de telefone é obrigatório para chave do tipo PHONE."
                    );
                }

                String cleanPhone = requestValue.replaceAll("\\D", "");

                if (cleanPhone.length() < 10 || cleanPhone.length() > 11) {
                    throw new InvalidDataException(
                            "keyValue",
                            "Número de telefone inválido. Deve conter 10 ou 11 dígitos."
                    );
                }

                if (cleanPhone.startsWith("0")) {
                    throw new InvalidDataException(
                            "keyValue",
                            "Número de telefone inválido. Não pode começar com zero."
                    );
                }

                yield cleanPhone;
            }
        };
    }

    @Transactional(readOnly = true)
    public List<PixKeyResponse> listKeysByUserId(UUID userId) {
        return pixKeyRepository.findAllByAccountUserId(userId)
                .stream()
                .map(pixKeyMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteKey(UUID keyId, UUID userId) {
        PixKey pixKey = pixKeyRepository.findById(keyId)
                .orElseThrow(() -> new ResourceNotFoundException("Chave PIX", keyId.toString()));

        if (!pixKey.getAccount().getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Chave PIX", keyId.toString());
        }

        pixKeyRepository.delete(pixKey);

        auditLogService.log(new AuditLogRequest(
                userId,
                "PIX_KEY_DELETED",
                "Chave PIX excluída: " + pixKey.getKeyValue()
        ));
    }
}