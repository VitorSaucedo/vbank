package com.vitorsaucedo.vbank.entities;

import com.vitorsaucedo.vbank.entities.enums.TransactionStatus;
import com.vitorsaucedo.vbank.entities.enums.TransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payer_account_id", nullable = false)
    private BankAccount payer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payee_account_id", nullable = false)
    private BankAccount payee;

    @NotNull
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @Column(unique = true)
    private UUID idempotencyKey; // Proteção contra cliques duplos

    private String description;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
