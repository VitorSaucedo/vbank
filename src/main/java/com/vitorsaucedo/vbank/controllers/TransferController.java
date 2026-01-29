package com.vitorsaucedo.vbank.controllers;

import com.vitorsaucedo.vbank.dtos.PixKeyDetailsResponse;
import com.vitorsaucedo.vbank.dtos.PixTransferRequest;
import com.vitorsaucedo.vbank.dtos.TransactionResponse; // Importando o DTO de resposta
import com.vitorsaucedo.vbank.entities.User;
import com.vitorsaucedo.vbank.services.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        TransactionResponse response = transferService.executePix(request, user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/check-receiver/{key}")
    public ResponseEntity<PixKeyDetailsResponse> checkReceiver(@PathVariable String key) {
        PixKeyDetailsResponse response = transferService.findReceiverByPixKey(key);
        return ResponseEntity.ok(response);
    }
}