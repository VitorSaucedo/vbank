package com.vitorsaucedo.vbank.controllers;

import com.vitorsaucedo.vbank.dtos.StatementItemResponse;
import com.vitorsaucedo.vbank.entities.User;
import com.vitorsaucedo.vbank.services.TransactionService;
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
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/statement")
    public ResponseEntity<List<StatementItemResponse>> getStatement(@AuthenticationPrincipal User user) {
        List<StatementItemResponse> statement = transactionService.getStatement(user.getId());
        return ResponseEntity.ok(statement);
    }
}