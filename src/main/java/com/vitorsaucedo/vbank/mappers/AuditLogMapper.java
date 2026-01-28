package com.vitorsaucedo.vbank.mappers;

import com.vitorsaucedo.vbank.dtos.AuditLogRequest;
import com.vitorsaucedo.vbank.dtos.AuditLogResponse;
import com.vitorsaucedo.vbank.entities.AuditLog;
import org.springframework.stereotype.Component;

@Component
public class AuditLogMapper {

    public AuditLog toEntity(AuditLogRequest request) {
        AuditLog auditLog = new AuditLog();
        auditLog.setUserId(request.userId());
        auditLog.setAction(request.action());
        auditLog.setDetails(request.details());
        return auditLog;
    }

    public AuditLogResponse toResponse(AuditLog auditLog) {
        return new AuditLogResponse(
                auditLog.getAction(),
                auditLog.getDetails(),
                auditLog.getCreatedAt()
        );
    }
}
