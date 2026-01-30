package com.vitorsaucedo.vbank.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Schema(description = "Entidade que representa um usuário do sistema bancário")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Identificador único do usuário", example = "123e4567-e89b-12d3-a456-426614174006")
    private UUID id;

    @NotBlank
    @Column(nullable = false)
    @Schema(description = "Nome completo do usuário", example = "João da Silva", required = true)
    private String fullName;

    @NotBlank
    @Column(unique = true, nullable = false)
    @Schema(description = "Documento de identificação (CPF ou CNPJ)", example = "12345678900", required = true)
    private String document;

    @Email
    @NotBlank
    @Column(unique = true, nullable = false)
    @Schema(description = "Endereço de e-mail do usuário (utilizado como username)", example = "joao.silva@email.com", required = true)
    private String email;

    @NotBlank
    @Column(nullable = false)
    @Schema(description = "Senha criptografada do usuário", example = "$2a$10$...", required = true, accessMode = Schema.AccessMode.WRITE_ONLY)
    private String password;

    @NotBlank
    @Column(nullable = false)
    @Schema(description = "PIN de segurança para transações (criptografado)", example = "$2a$10$...", required = true, accessMode = Schema.AccessMode.WRITE_ONLY)
    private String transactionPin;

    @CreationTimestamp
    @Schema(description = "Data e hora de criação do cadastro do usuário", example = "2025-01-29T10:15:30", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @Schema(description = "Conta bancária associada ao usuário")
    private BankAccount account;

    // --- MÉTODOS DO USERDETAILS ---

    @Override
    @Schema(hidden = true)
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    @Schema(hidden = true)
    public String getUsername() {
        return this.email;
    }

    @Override
    @Schema(hidden = true)
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @Schema(hidden = true)
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @Schema(hidden = true)
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @Schema(hidden = true)
    public boolean isEnabled() {
        return true;
    }
}