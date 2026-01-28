package com.vitorsaucedo.vbank.repositories;

import com.vitorsaucedo.vbank.entities.PixKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PixKeyRepository extends JpaRepository<PixKey, UUID> {

    boolean existsByKeyValueAndAccountId(String keyValue, UUID accountId);

    // Busca a conta vinculada a uma chave (E-mail, CPF, etc.)
    Optional<PixKey> findByKeyValue(String keyValue);

    // Verifica se a chave já pertence a alguém
    boolean existsByKeyValue(String keyValue);

    List<PixKey> findAllByAccountUserId(UUID userId);
}
