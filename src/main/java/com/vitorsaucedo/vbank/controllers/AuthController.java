package com.vitorsaucedo.vbank.controllers;

import com.vitorsaucedo.vbank.config.GlobalExceptionHandler;
import com.vitorsaucedo.vbank.dtos.LoginRequest;
import com.vitorsaucedo.vbank.dtos.LoginResponse;
import com.vitorsaucedo.vbank.dtos.UserRegistrationRequest;
import com.vitorsaucedo.vbank.dtos.UserResponse;
import com.vitorsaucedo.vbank.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints para registro e autenticação de usuários")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(
            summary = "Registrar novo usuário",
            description = "Cria uma nova conta de usuário no sistema com dados pessoais e credenciais"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuário registrado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class),
                            examples = @ExampleObject(
                                    name = "Usuário criado",
                                    value = """
                    {
                        "id": "123e4567-e89b-12d3-a456-426614174000",
                        "fullName": "João da Silva Santos",
                        "email": "joao.silva@email.com",
                        "accountNumber": "0001234567",
                        "agency": "0001"
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro de validação nos dados enviados",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Erro de validação",
                                    value = """
                    {
                        "status": 400,
                        "message": "Erro de validação nos dados enviados.",
                        "timestamp": "2025-01-29T10:15:30",
                        "errors": {
                            "email": "E-mail inválido",
                            "password": "A senha de acesso deve ter no mínimo 8 caracteres",
                            "transactionPin": "O PIN deve conter apenas números"
                        }
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "E-mail ou documento já cadastrado (DuplicateResourceException)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Recurso duplicado",
                                    value = """
                    {
                        "status": 409,
                        "message": "Usuário já cadastrado(a) com este(a) e-mail.",
                        "timestamp": "2025-01-29T10:15:30",
                        "errors": null
                    }
                    """
                            )
                    )
            )
    })
    public ResponseEntity<UserResponse> register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados para registro do novo usuário",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserRegistrationRequest.class),
                            examples = @ExampleObject(
                                    name = "Exemplo de registro",
                                    value = """
                        {
                            "fullName": "João da Silva Santos",
                            "document": "12345678900",
                            "email": "joao.silva@email.com",
                            "password": "minhasenha123",
                            "transactionPin": "1234"
                        }
                        """
                            )
                    )
            )
            @RequestBody @Valid UserRegistrationRequest request) {
        UserResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    @Operation(
            summary = "Realizar login",
            description = "Autentica um usuário e retorna um token JWT para acesso aos endpoints protegidos"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login realizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponse.class),
                            examples = @ExampleObject(
                                    name = "Login bem-sucedido",
                                    value = """
                    {
                        "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                        "type": "Bearer",
                        "expiresIn": 3600
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro de validação nos dados enviados",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Validação falhou",
                                    value = """
                    {
                        "status": 400,
                        "message": "Erro de validação nos dados enviados.",
                        "timestamp": "2025-01-29T10:15:30",
                        "errors": {
                            "email": "O e-mail é obrigatório",
                            "password": "A senha de acesso é obrigatória"
                        }
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Credenciais inválidas (InvalidCredentialsException)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Credenciais inválidas",
                                    value = """
                    {
                        "status": 401,
                        "message": "Email ou senha incorretos.",
                        "timestamp": "2025-01-29T10:15:30",
                        "errors": null
                    }
                    """
                            )
                    )
            )
    })
    public ResponseEntity<LoginResponse> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Credenciais de login do usuário",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginRequest.class),
                            examples = @ExampleObject(
                                    name = "Exemplo de login",
                                    value = """
                        {
                            "email": "joao.silva@email.com",
                            "password": "minhasenha123"
                        }
                        """
                            )
                    )
            )
            @RequestBody @Valid LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}