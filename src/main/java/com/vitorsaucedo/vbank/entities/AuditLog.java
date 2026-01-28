package com.vitorsaucedo.vbank.entities;

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
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID userId; // Quem realizou a ação

    @Column(nullable = false)
    private String action; // Ex: "LOGIN_FAILED", "TRANSACTION_PIN_CHANGED", "ACCOUNT_BLOCKED"

    @Column(columnDefinition = "TEXT")
    private String details; // Detalhes técnicos (ex: IP, User-Agent ou descrição do erro)

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
