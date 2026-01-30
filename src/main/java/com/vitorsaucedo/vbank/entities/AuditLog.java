package com.vitorsaucedo.vbank.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Schema(description = "Entidade que representa os logs de auditoria do sistema")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Identificador único do log de auditoria", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Column(nullable = false)
    @Schema(description = "Identificador do usuário que realizou a ação", example = "123e4567-e89b-12d3-a456-426614174001", required = true)
    private UUID userId;

    @Column(nullable = false)
    @Schema(description = "Tipo de ação realizada", example = "LOGIN_FAILED", required = true, allowableValues = {"LOGIN_FAILED", "TRANSACTION_PIN_CHANGED", "ACCOUNT_BLOCKED"})
    private String action;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "Detalhes técnicos da ação realizada (IP, User-Agent, descrição do erro, etc.)", example = "IP: 192.168.1.1, User-Agent: Mozilla/5.0")
    private String details;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    @Schema(description = "Data e hora de criação do log", example = "2025-01-29T10:15:30", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;
}
