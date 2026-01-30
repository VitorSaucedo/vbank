package com.vitorsaucedo.vbank.controllers;

import com.vitorsaucedo.vbank.config.GlobalExceptionHandler;
import com.vitorsaucedo.vbank.dtos.StatementItemResponse;
import com.vitorsaucedo.vbank.entities.User;
import com.vitorsaucedo.vbank.services.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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

import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
@Tag(name = "Transações", description = "Endpoints para consulta de transações e extrato")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/statement")
    @Operation(
            summary = "Obter extrato bancário",
            description = "Retorna o histórico completo de transações (enviadas e recebidas) do usuário autenticado, ordenado da mais recente para a mais antiga"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Extrato retornado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = StatementItemResponse.class)),
                            examples = @ExampleObject(
                                    name = "Extrato com transações",
                                    value = """
                                    [
                                        {
                                            "id": "123e4567-e89b-12d3-a456-426614174001",
                                            "amount": 150.50,
                                            "date": "2025-01-29T10:15:30",
                                            "description": "Pagamento de aluguel",
                                            "otherPartyName": "Maria Souza",
                                            "type": "PIX",
                                            "direction": "OUTBOUND"
                                        },
                                        {
                                            "id": "123e4567-e89b-12d3-a456-426614174002",
                                            "amount": 500.00,
                                            "date": "2025-01-28T14:30:00",
                                            "description": "Transferência recebida",
                                            "otherPartyName": "Pedro Santos",
                                            "type": "PIX",
                                            "direction": "INBOUND"
                                        }
                                    ]
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
                                    name = "Token inválido",
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
    public ResponseEntity<List<StatementItemResponse>> getStatement(
            @Parameter(hidden = true) @AuthenticationPrincipal User user) {
        List<StatementItemResponse> statement = transactionService.getStatement(user.getId());
        return ResponseEntity.ok(statement);
    }
}