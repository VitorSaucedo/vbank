package com.vitorsaucedo.vbank.repositories;

import com.vitorsaucedo.vbank.entities.BankAccount;
import com.vitorsaucedo.vbank.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    boolean existsByIdempotencyKey(UUID idempotencyKey);

    // Busca todas as transações onde a conta foi pagadora OU recebedora
    @Query("SELECT t FROM Transaction t WHERE t.payer = :account OR t.payee = :account ORDER BY t.createdAt DESC")
    List<Transaction> findAllByAccountOrderByCreatedAtDesc(BankAccount account);

}
