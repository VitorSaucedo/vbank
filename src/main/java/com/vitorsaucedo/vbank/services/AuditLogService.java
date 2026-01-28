package com.vitorsaucedo.vbank.services;

import com.vitorsaucedo.vbank.dtos.AuditLogRequest;
import com.vitorsaucedo.vbank.entities.AuditLog;
import com.vitorsaucedo.vbank.mappers.AuditLogMapper;
import com.vitorsaucedo.vbank.repositories.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper; // Injeção do Mapper de auditoria

    @Transactional
    public void log(AuditLogRequest request) {
        // 1. Converte o DTO de requisição para a Entidade AuditLog
        AuditLog auditLog = auditLogMapper.toEntity(request);

        // 2. Persiste o registro no banco de dados
        auditLogRepository.save(auditLog);
    }
}