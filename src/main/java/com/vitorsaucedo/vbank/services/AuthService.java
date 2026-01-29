package com.vitorsaucedo.vbank.services;

import com.vitorsaucedo.vbank.dtos.AuditLogRequest;
import com.vitorsaucedo.vbank.dtos.LoginRequest;
import com.vitorsaucedo.vbank.dtos.LoginResponse;
import com.vitorsaucedo.vbank.dtos.UserRegistrationRequest;
import com.vitorsaucedo.vbank.dtos.UserResponse;
import com.vitorsaucedo.vbank.entities.BankAccount;
import com.vitorsaucedo.vbank.entities.User;
import com.vitorsaucedo.vbank.exceptions.DuplicateResourceException;
import com.vitorsaucedo.vbank.exceptions.InvalidCredentialsException;
import com.vitorsaucedo.vbank.exceptions.InvalidDataException;
import com.vitorsaucedo.vbank.mappers.UserMapper;
import com.vitorsaucedo.vbank.repositories.UserRepository;
import com.vitorsaucedo.vbank.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditLogService auditLogService;
    private final UserMapper userMapper;

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public LoginResponse login(LoginRequest request) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(
                    request.email(),
                    request.password()
            );

            var auth = this.authenticationManager.authenticate(usernamePassword);
            var user = (User) auth.getPrincipal();
            var token = tokenService.generateToken(user);

            auditLogService.log(new AuditLogRequest(
                    user.getId(),
                    "USER_LOGIN",
                    "Login realizado com sucesso."
            ));

            return new LoginResponse(token, "Bearer", 7200L);

        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException();
        } catch (AuthenticationException e) {
            throw new InvalidCredentialsException("Erro ao autenticar. Verifique suas credenciais.");
        }
    }

    @Transactional
    public UserResponse register(UserRegistrationRequest request) {
        validateUserUniqueness(request);
        validateRegistrationData(request);

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setTransactionPin(passwordEncoder.encode(request.transactionPin()));

        createAutomaticAccount(user);

        User savedUser = userRepository.save(user);

        auditLogService.log(new AuditLogRequest(
                savedUser.getId(),
                "USER_REGISTERED",
                "Nova conta criada: " + savedUser.getAccount().getAccountNumber()
        ));

        return userMapper.toResponse(savedUser);
    }

    private void validateUserUniqueness(UserRegistrationRequest request) {
        if (userRepository.existsByDocument(request.document())) {
            throw new DuplicateResourceException("Usuário", "documento");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Usuário", "email");
        }
    }

    private void validateRegistrationData(UserRegistrationRequest request) {
        // Validação de CPF (11 dígitos sem formatação)
        String cleanDocument = request.document().replaceAll("\\D", "");
        if (cleanDocument.length() != 11 && cleanDocument.length() != 14) {
            throw new InvalidDataException(
                    "document",
                    "Documento inválido. Deve conter 11 dígitos (CPF) ou 14 dígitos (CNPJ)."
            );
        }

        if (request.password() == null || request.password().length() < 6) {
            throw new InvalidDataException(
                    "password",
                    "A senha deve conter no mínimo 6 caracteres."
            );
        }

        if (request.transactionPin() == null || !request.transactionPin().matches("\\d{4}")) {
            throw new InvalidDataException(
                    "transactionPin",
                    "O PIN de transação deve conter exatamente 4 dígitos numéricos."
            );
        }

        if (request.fullName() == null || request.fullName().trim().isEmpty()) {
            throw new InvalidDataException(
                    "fullName",
                    "O nome completo é obrigatório."
            );
        }

        if (request.email() == null || !request.email().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new InvalidDataException(
                    "email",
                    "Email inválido."
            );
        }
    }

    private void createAutomaticAccount(User user) {
        BankAccount account = new BankAccount();
        account.setAccountNumber(generateAccountNumber());
        account.setBalance(BigDecimal.ZERO);
        account.setAgency("0001"); // Agência padrão
        account.setUser(user);
        user.setAccount(account);
    }

    private String generateAccountNumber() {
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }
}