package com.vitorsaucedo.vbank.entities;

import com.vitorsaucedo.vbank.entities.enums.PixKeyType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_pix_keys")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Schema(description = "Entidade que representa uma chave PIX vinculada a uma conta bancária")
public class PixKey {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Identificador único da chave PIX", example = "123e4567-e89b-12d3-a456-426614174003")
    private UUID id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Tipo da chave PIX", example = "EMAIL", required = true, allowableValues = {"CPF", "CNPJ", "EMAIL", "PHONE", "RANDOM"})
    private PixKeyType keyType;

    @NotBlank
    @Column(unique = true, nullable = false)
    @Schema(description = "Valor da chave PIX", example = "usuario@email.com", required = true)
    private String keyValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    @Schema(description = "Conta bancária à qual a chave PIX está vinculada", required = true)
    private BankAccount account;

    @CreationTimestamp
    @Schema(description = "Data e hora de criação da chave PIX", example = "2025-01-29T10:15:30", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;
}
