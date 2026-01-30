package com.vitorsaucedo.vbank.controllers;

import com.vitorsaucedo.vbank.config.GlobalExceptionHandler;
import com.vitorsaucedo.vbank.dtos.PixKeyRequest;
import com.vitorsaucedo.vbank.dtos.PixKeyResponse;
import com.vitorsaucedo.vbank.entities.User;
import com.vitorsaucedo.vbank.services.PixKeyService;
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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/pix-keys")
@RequiredArgsConstructor
@Tag(name = "Chaves PIX", description = "Endpoints para gerenciamento de chaves PIX")
public class PixKeyController {

    private final PixKeyService pixKeyService;

    @PostMapping
    @Operation(
            summary = "Criar nova chave PIX",
            description = "Registra uma nova chave PIX vinculada à conta do usuário autenticado. Para tipo RANDOM, o valor da chave é gerado automaticamente."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Chave PIX criada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PixKeyResponse.class),
                            examples = @ExampleObject(
                                    name = "Chave criada",
                                    value = """
                                    {
                                        "id": "123e4567-e89b-12d3-a456-426614174000",
                                        "keyType": "EMAIL",
                                        "keyValue": "joao.silva@email.com"
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
                                            "keyType": "O tipo da chave é obrigatório"
                                        }
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
                    responseCode = "409",
                    description = "Chave PIX já cadastrada (DuplicateResourceException)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Chave duplicada",
                                    value = """
                                    {
                                        "status": 409,
                                        "message": "Chave PIX já cadastrado(a) com este(a) valor.",
                                        "timestamp": "2025-01-29T10:15:30",
                                        "errors": null
                                    }
                                    """
                            )
                    )
            )
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<PixKeyResponse> createKey(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados da chave PIX a ser criada",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PixKeyRequest.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Chave tipo E-mail",
                                            value = """
                                            {
                                                "keyType": "EMAIL",
                                                "keyValue": "joao.silva@email.com"
                                            }
                                            """
                                    ),
                                    @ExampleObject(
                                            name = "Chave tipo CPF",
                                            value = """
                                            {
                                                "keyType": "CPF",
                                                "keyValue": "12345678900"
                                            }
                                            """
                                    ),
                                    @ExampleObject(
                                            name = "Chave aleatória (sem valor)",
                                            value = """
                                            {
                                                "keyType": "RANDOM",
                                                "keyValue": null
                                            }
                                            """
                                    )
                            }
                    )
            )
            @RequestBody @Valid PixKeyRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal User user) {
        PixKeyResponse response = pixKeyService.createKey(request, user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(
            summary = "Listar minhas chaves PIX",
            description = "Retorna todas as chaves PIX cadastradas pelo usuário autenticado"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de chaves PIX retornada com sucesso (pode ser vazia)",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PixKeyResponse.class)),
                            examples = @ExampleObject(
                                    name = "Lista de chaves",
                                    value = """
                                    [
                                        {
                                            "id": "123e4567-e89b-12d3-a456-426614174001",
                                            "keyType": "EMAIL",
                                            "keyValue": "joao.silva@email.com"
                                        },
                                        {
                                            "id": "123e4567-e89b-12d3-a456-426614174002",
                                            "keyType": "CPF",
                                            "keyValue": "12345678900"
                                        },
                                        {
                                            "id": "123e4567-e89b-12d3-a456-426614174003",
                                            "keyType": "RANDOM",
                                            "keyValue": "a1b2c3d4-e5f6-7890-abcd-ef1234567890"
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
            )
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<PixKeyResponse>> listMyKeys(
            @Parameter(hidden = true) @AuthenticationPrincipal User user) {
        List<PixKeyResponse> keys = pixKeyService.listKeysByUserId(user.getId());
        return ResponseEntity.ok(keys);
    }

    @DeleteMapping("/{keyId}")
    @Operation(
            summary = "Excluir chave PIX",
            description = "Remove uma chave PIX específica da conta do usuário autenticado"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Chave PIX excluída com sucesso"
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
                    responseCode = "403",
                    description = "Usuário não tem permissão para excluir esta chave (BusinessException)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Sem permissão",
                                    value = """
                                    {
                                        "status": 400,
                                        "message": "Você não tem permissão para excluir esta chave PIX.",
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
                                    name = "Chave não encontrada",
                                    value = """
                                    {
                                        "status": 404,
                                        "message": "Chave PIX não encontrado(a).",
                                        "timestamp": "2025-01-29T10:15:30",
                                        "errors": null
                                    }
                                    """
                            )
                    )
            )
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> deleteKey(
            @Parameter(description = "ID da chave PIX a ser excluída", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID keyId,
            @Parameter(hidden = true) @AuthenticationPrincipal User user) {
        pixKeyService.deleteKey(keyId, user.getId());
        return ResponseEntity.noContent().build();
    }
}
