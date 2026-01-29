package com.vitorsaucedo.vbank.services;

import com.vitorsaucedo.vbank.dtos.AuditLogRequest;
import com.vitorsaucedo.vbank.entities.AuditLog;
import com.vitorsaucedo.vbank.mappers.AuditLogMapper;
import com.vitorsaucedo.vbank.repositories.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;

    @Transactional
    public void log(AuditLogRequest request) {
        AuditLog auditLog = auditLogMapper.toEntity(request);
        auditLogRepository.save(auditLog);
    }

    @Transactional(readOnly = true)
    public List<AuditLog> findByUserId(UUID userId) {
        return auditLogRepository.findAllByUserIdOrderByCreatedAtDesc(userId);
    }
}