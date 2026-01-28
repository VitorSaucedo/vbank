package com.vitorsaucedo.vbank.controllers;

import com.vitorsaucedo.vbank.dtos.PixKeyDetailsResponse;
import com.vitorsaucedo.vbank.dtos.PixTransferRequest;
import com.vitorsaucedo.vbank.dtos.TransactionResponse; // Importando o DTO de resposta
import com.vitorsaucedo.vbank.entities.User;
import com.vitorsaucedo.vbank.services.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping("/pix")
    public ResponseEntity<TransactionResponse> executePix(
            @RequestBody @Valid PixTransferRequest request,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(transferService.executePix(request, user.getId()));
    }

    @GetMapping("/check-receiver/{key}")
    public ResponseEntity<PixKeyDetailsResponse> checkReceiver(@PathVariable String key) {
        return ResponseEntity.ok(transferService.findReceiverByPixKey(key));
    }
}