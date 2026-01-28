package com.vitorsaucedo.vbank.services;

import com.vitorsaucedo.vbank.dtos.AuditLogRequest;
import com.vitorsaucedo.vbank.dtos.PixKeyRequest;
import com.vitorsaucedo.vbank.dtos.PixKeyResponse;
import com.vitorsaucedo.vbank.entities.BankAccount;
import com.vitorsaucedo.vbank.entities.PixKey;
import com.vitorsaucedo.vbank.entities.enums.PixKeyType;
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
    private final PixKeyMapper pixKeyMapper; // Injeção do Mapper

    /**
     * Cria uma nova chave Pix vinculada à conta do usuário.
     */
    @Transactional
    public PixKeyResponse createKey(PixKeyRequest request, UUID userId) {
        // 1. Busca a conta no banco
        BankAccount account = accountRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Conta bancária não encontrada."));

        // 2. Valida unicidade global da chave
        if (pixKeyRepository.existsByKeyValue(request.keyValue())) {
            throw new RuntimeException("Esta chave Pix já está em uso por outro usuário.");
        }

        // 3. Lógica de Negócio: Definição do valor da chave
        String value = (request.keyType() == PixKeyType.RANDOM)
                ? UUID.randomUUID().toString()
                : request.keyValue();

        // 4. Instancia e configura a entidade
        PixKey pixKey = new PixKey();
        pixKey.setAccount(account);
        pixKey.setKeyType(request.keyType());
        pixKey.setKeyValue(value);

        // 5. Persistência
        PixKey savedKey = pixKeyRepository.save(pixKey);

        // 6. Auditoria
        auditLogService.log(new AuditLogRequest(
                userId,
                "PIX_KEY_CREATED",
                "Nova chave do tipo " + request.keyType() + " cadastrada."
        ));

        // 7. Conversão para DTO via Mapper
        return pixKeyMapper.toResponse(savedKey);
    }

    /**
     * Lista todas as chaves do usuário de forma performática.
     */
    @Transactional(readOnly = true)
    public List<PixKeyResponse> listKeysByUserId(UUID userId) {
        return pixKeyRepository.findAllByAccountUserId(userId)
                .stream()
                .map(pixKeyMapper::toResponse) // Conversão delegada ao Mapper
                .collect(Collectors.toList());
    }
}