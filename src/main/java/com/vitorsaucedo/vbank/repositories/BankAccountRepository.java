package com.vitorsaucedo.vbank.repositories;

import com.vitorsaucedo.vbank.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface BankAccountRepository extends JpaRepository<BankAccount, UUID> {
    Optional<BankAccount> findByAccountNumber(String accountNumber);
    Optional<BankAccount> findByUserEmail(String email);
    Optional<BankAccount> findByUserId(UUID userId);
}
