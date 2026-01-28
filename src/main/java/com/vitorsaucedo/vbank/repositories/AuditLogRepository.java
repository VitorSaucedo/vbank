package com.vitorsaucedo.vbank.repositories;

import com.vitorsaucedo.vbank.entities.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {
    List<AuditLog> findAllByUserIdOrderByCreatedAtDesc(UUID userId);
}
