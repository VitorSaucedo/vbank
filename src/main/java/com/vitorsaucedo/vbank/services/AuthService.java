package com.vitorsaucedo.vbank.services;

import com.vitorsaucedo.vbank.dtos.*;
import com.vitorsaucedo.vbank.entities.BankAccount;
import com.vitorsaucedo.vbank.entities.User;
import com.vitorsaucedo.vbank.mappers.UserMapper;
import com.vitorsaucedo.vbank.repositories.UserRepository;
import com.vitorsaucedo.vbank.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
        var usernamePassword = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var user = (User) auth.getPrincipal();
        var token = tokenService.generateToken(user);

        auditLogService.log(new AuditLogRequest(
                user.getId(),
                "USER_LOGIN",
                "Login realizado com sucesso."
        ));

        return new LoginResponse(token, "Bearer", 7200L);
    }

    /**
     * Realiza o registro do usuário, criptografia de dados sensíveis e criação de conta.
     */
    @Transactional
    public UserResponse register(UserRegistrationRequest request) {
        validateUserUniqueness(request);

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

    // Métodos auxiliares privados...
    private void validateUserUniqueness(UserRegistrationRequest request) {
        if (userRepository.existsByDocument(request.document())) {
            throw new RuntimeException("Documento já cadastrado.");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("E-mail já cadastrado.");
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