package com.vitorsaucedo.vbank.controllers;

import com.vitorsaucedo.vbank.dtos.PixKeyRequest;
import com.vitorsaucedo.vbank.dtos.PixKeyResponse;
import com.vitorsaucedo.vbank.entities.User;
import com.vitorsaucedo.vbank.services.PixKeyService;
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
public class PixKeyController {

    private final PixKeyService pixKeyService;

    @PostMapping
    public ResponseEntity<PixKeyResponse> createKey(
            @RequestBody @Valid PixKeyRequest request,
            @AuthenticationPrincipal User user) {
        PixKeyResponse response = pixKeyService.createKey(request, user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<PixKeyResponse>> listMyKeys(@AuthenticationPrincipal User user) {
        List<PixKeyResponse> keys = pixKeyService.listKeysByUserId(user.getId());
        return ResponseEntity.ok(keys);
    }

    @DeleteMapping("/{keyId}")
    public ResponseEntity<Void> deleteKey(
            @PathVariable UUID keyId,
            @AuthenticationPrincipal User user) {
        pixKeyService.deleteKey(keyId, user.getId());
        return ResponseEntity.noContent().build();
    }
}
