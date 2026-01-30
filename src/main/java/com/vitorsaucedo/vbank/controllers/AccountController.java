package com.vitorsaucedo.vbank.controllers;

import com.vitorsaucedo.vbank.config.GlobalExceptionHandler;
import com.vitorsaucedo.vbank.dtos.AccountDashboardResponse;
import com.vitorsaucedo.vbank.entities.User;
import com.vitorsaucedo.vbank.services.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
@Tag(name = "Contas", description = "Endpoints para gerenciamento de contas bancárias")
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/dashboard")
    @Operation(
            summary = "Obter dashboard da conta",
            description = "Retorna informações consolidadas da conta do usuário autenticado, incluindo saldo, número da conta e estatísticas"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Dashboard retornado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AccountDashboardResponse.class),
                            examples = @ExampleObject(
                                    name = "Dashboard completo",
                                    value = """
                                    {
                                        "fullName": "João da Silva Santos",
                                        "accountNumber": "0001234567",
                                        "agency": "0001",
                                        "balance": 1250.75
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Usuário não autenticado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Token inválido ou expirado",
                                    value = """
                                    {
                                        "status": 401,
                                        "message": "Token JWT inválido ou expirado",
                                        "timestamp": "2025-01-29T10:15:30",
                                        "errors": null
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Conta não encontrada (ResourceNotFoundException)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Conta não encontrada",
                                    value = """
                                    {
                                        "status": 404,
                                        "message": "Conta não encontrado(a).",
                                        "timestamp": "2025-01-29T10:15:30",
                                        "errors": null
                                    }
                                    """
                            )
                    )
            )
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<AccountDashboardResponse> getDashboard(
            @Parameter(hidden = true) @AuthenticationPrincipal User user) {
        AccountDashboardResponse response = accountService.getDashboardData(user.getId());
        return ResponseEntity.ok(response);
    }
}