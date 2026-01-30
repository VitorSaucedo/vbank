package com.vitorsaucedo.vbank.entities;

import com.vitorsaucedo.vbank.entities.enums.AccountStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "tb_accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Schema(description = "Entidade que representa uma conta bancária no sistema")
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Identificador único da conta bancária", example = "123e4567-e89b-12d3-a456-426614174002")
    private UUID id;

    @Column(nullable = false, unique = true)
    @Schema(description = "Número único da conta bancária", example = "1234567890", required = true)
    private String accountNumber;

    @Column(nullable = false)
    @Schema(description = "Número da agência bancária", example = "0001", required = true, defaultValue = "0001")
    private String agency = "0001";

    @Column(nullable = false)
    @Schema(description = "Saldo atual da conta", example = "1500.00", required = true, defaultValue = "0.00")
    private BigDecimal balance = BigDecimal.ZERO;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @Schema(description = "Usuário titular da conta bancária")
    private User user;

    @Enumerated(EnumType.STRING)
    @Schema(
            description = "Status atual da conta bancária",
            example = "ACTIVE",
            defaultValue = "ACTIVE",
            implementation = AccountStatus.class
    )
    private AccountStatus status = AccountStatus.ACTIVE;

    // Métodos de domínio para garantir consistência
    public void deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor do depósito deve ser positivo.");
        }
        this.balance = this.balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor do saque deve ser positivo.");
        }
        if (this.balance.compareTo(amount) < 0) {
            throw new RuntimeException("Saldo insuficiente.");
        }
        this.balance = this.balance.subtract(amount);
    }
}
