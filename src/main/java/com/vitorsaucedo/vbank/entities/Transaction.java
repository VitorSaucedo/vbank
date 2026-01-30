package com.vitorsaucedo.vbank.entities;

import com.vitorsaucedo.vbank.entities.enums.TransactionStatus;
import com.vitorsaucedo.vbank.entities.enums.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Entidade que representa uma transação financeira entre contas")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Identificador único da transação", example = "123e4567-e89b-12d3-a456-426614174004")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payer_account_id", nullable = false)
    @Schema(description = "Conta bancária do pagador (quem envia o dinheiro)", required = true)
    private BankAccount payer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payee_account_id", nullable = false)
    @Schema(description = "Conta bancária do beneficiário (quem recebe o dinheiro)", required = true)
    private BankAccount payee;

    @NotNull
    @Column(nullable = false, precision = 19, scale = 2)
    @Schema(description = "Valor da transação", example = "250.50", required = true)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Tipo da transação", example = "PIX", required = true, allowableValues = {"PIX", "TED", "DOC", "DEPOSIT", "WITHDRAWAL"})
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Status atual da transação", example = "COMPLETED", required = true, allowableValues = {"PENDING", "COMPLETED", "FAILED", "CANCELLED"})
    private TransactionStatus status;

    @Column(unique = true)
    @Schema(description = "Chave de idempotência para evitar transações duplicadas", example = "123e4567-e89b-12d3-a456-426614174005")
    private UUID idempotencyKey;

    @Schema(description = "Descrição ou observação da transação", example = "Pagamento de aluguel")
    private String description;

    @CreationTimestamp
    @Schema(description = "Data e hora de criação da transação", example = "2025-01-29T10:15:30", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;
}
