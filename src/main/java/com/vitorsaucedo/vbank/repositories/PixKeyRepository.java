package com.vitorsaucedo.vbank.repositories;

import com.vitorsaucedo.vbank.entities.PixKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PixKeyRepository extends JpaRepository<PixKey, UUID> {

    boolean existsByKeyValueAndAccountId(String keyValue, UUID accountId);

    Optional<PixKey> findByKeyValue(String keyValue);

    boolean existsByKeyValue(String keyValue);

    List<PixKey> findAllByAccountUserId(UUID userId);
}
