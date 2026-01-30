package com.vitorsaucedo.vbank.controllers;

import com.vitorsaucedo.vbank.config.GlobalExceptionHandler;
import com.vitorsaucedo.vbank.dtos.PixKeyDetailsResponse;
import com.vitorsaucedo.vbank.dtos.PixTransferRequest;
import com.vitorsaucedo.vbank.dtos.TransactionResponse; // Importando o DTO de resposta
import com.vitorsaucedo.vbank.entities.User;
import com.vitorsaucedo.vbank.services.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transfers")
@RequiredArgsConstructor
@Tag(name = "Transferências", description = "Endpoints para realização de transferências bancárias")
public class TransferController {

    private final TransferService transferService;

    @PostMapping("/pix")
    @Operation(
            summary = "Realizar transferência PIX",
            description = "Executa uma transferência PIX da conta do usuário autenticado para a chave PIX informada"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Transferência PIX realizada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TransactionResponse.class),
                            examples = @ExampleObject(
                                    name = "Transferência bem-sucedida",
                                    value = """
                    {
                        "transactionId": "123e4567-e89b-12d3-a456-426614174000",
                        "amount": 150.50,
                        "timestamp": "2025-01-29T10:15:30",
                        "description": "Pagamento de aluguel",
                        "status": "COMPLETED",
                        "payer": {
                            "name": "João da Silva Santos",
                            "document": "12345678900",
                            "bank": "Vbank",
                            "agency": "0001",
                            "account": "0001234567"
                        },
                        "payee": {
                            "name": "Maria Souza",
                            "document": "98765432100",
                            "bank": "Vbank",
                            "agency": "0001",
                            "account": "0009876543"
                        }
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro de validação ou dados inválidos (InvalidDataException)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Validação falhou",
                                            value = """
                        {
                            "status": 400,
                            "message": "Erro de validação nos dados enviados.",
                            "timestamp": "2025-01-29T10:15:30",
                            "errors": {
                                "amount": "O valor é obrigatório",
                                "targetKey": "A chave Pix de destino é obrigatória"
                            }
                        }
                        """
                                    ),
                                    @ExampleObject(
                                            name = "Transferência para mesma conta",
                                            value = """
                        {
                            "status": 400,
                            "message": "Não é possível transferir para a própria conta.",
                            "timestamp": "2025-01-29T10:15:30",
                            "errors": null
                        }
                        """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Usuário não autenticado ou PIN inválido (InvalidPinException)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "PIN inválido",
                                    value = """
                    {
                        "status": 401,
                        "message": "PIN de transação incorreto. Verifique e tente novamente.",
                        "timestamp": "2025-01-29T10:15:30",
                        "errors": null
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Conta inativa (InactiveAccountException)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Conta inativa",
                                    value = """
                    {
                        "status": 403,
                        "message": "A conta está inativa e não pode realizar transações.",
                        "timestamp": "2025-01-29T10:15:30",
                        "errors": null
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Chave PIX não encontrada (ResourceNotFoundException)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Chave PIX não encontrada",
                                    value = """
                    {
                        "status": 404,
                        "message": "Chave PIX não encontrado(a) com identificador: inexistente@email.com",
                        "timestamp": "2025-01-29T10:15:30",
                        "errors": null
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Saldo insuficiente (InsufficientBalanceException)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Saldo insuficiente",
                                    value = """
                    {
                        "status": 422,
                        "message": "Saldo insuficiente. Disponível: R$ 100,00, Necessário: R$ 150,50",
                        "timestamp": "2025-01-29T10:15:30",
                        "errors": null
                    }
                    """
                            )
                    )
            )
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<TransactionResponse> executePix(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados da transferência PIX a ser realizada",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PixTransferRequest.class),
                            examples = @ExampleObject(
                                    name = "Exemplo de transferência PIX",
                                    value = """
                        {
                            "targetKey": "destinatario@email.com",
                            "amount": 150.50,
                            "transactionPin": "1234",
                            "description": "Pagamento de aluguel"
                        }
                        """
                            )
                    )
            )
            @RequestBody @Valid PixTransferRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal User user) {
        TransactionResponse response = transferService.executePix(request, user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/check-receiver/{key}")
    @Operation(
            summary = "Consultar dados do destinatário",
            description = "Retorna informações do titular da chave PIX antes de realizar a transferência"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Dados do destinatário retornados com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PixKeyDetailsResponse.class),
                            examples = @ExampleObject(
                                    name = "Destinatário encontrado",
                                    value = """
                    {
                        "fullName": "Ma*** S****",
                        "document": "***456789**",
                        "bankName": "Vbank",
                        "accountNumber": "0009876543",
                        "agency": "0001"
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Chave PIX não encontrada (ResourceNotFoundException)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Chave não encontrada",
                                    value = """
                    {
                        "status": 404,
                        "message": "Chave PIX não encontrado(a) com identificador: inexistente@email.com",
                        "timestamp": "2025-01-29T10:15:30",
                        "errors": null
                    }
                    """
                            )
                    )
            )
    })
    public ResponseEntity<PixKeyDetailsResponse> checkReceiver(
            @Parameter(description = "Chave PIX do destinatário", required = true, example = "destinatario@email.com")
            @PathVariable String key) {
        PixKeyDetailsResponse response = transferService.findReceiverByPixKey(key);
        return ResponseEntity.ok(response);
    }
}