package com.vitorsaucedo.vbank.controllers;

import com.vitorsaucedo.vbank.dtos.AccountDashboardResponse;
import com.vitorsaucedo.vbank.entities.User;
import com.vitorsaucedo.vbank.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/dashboard")
    public ResponseEntity<AccountDashboardResponse> getDashboard(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(accountService.getDashboardData(user.getId()));
    }
}