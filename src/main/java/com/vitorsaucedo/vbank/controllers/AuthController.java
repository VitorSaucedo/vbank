package com.vitorsaucedo.vbank.controllers;

import com.vitorsaucedo.vbank.dtos.LoginRequest;
import com.vitorsaucedo.vbank.dtos.LoginResponse;
import com.vitorsaucedo.vbank.dtos.UserRegistrationRequest;
import com.vitorsaucedo.vbank.dtos.UserResponse; // Importado para a resposta completa
import com.vitorsaucedo.vbank.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody @Valid UserRegistrationRequest request) {
        // O Service agora retorna o DTO com ID e número da conta
        UserResponse response = authService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}