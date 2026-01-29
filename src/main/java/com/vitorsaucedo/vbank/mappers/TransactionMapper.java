package com.vitorsaucedo.vbank.mappers;

import com.vitorsaucedo.vbank.dtos.PixTransferRequest;
import com.vitorsaucedo.vbank.dtos.StatementItemResponse;
import com.vitorsaucedo.vbank.dtos.TransactionResponse;
import com.vitorsaucedo.vbank.entities.BankAccount;
import com.vitorsaucedo.vbank.entities.Transaction;
import com.vitorsaucedo.vbank.entities.enums.TransactionDirection;
import com.vitorsaucedo.vbank.entities.enums.TransactionStatus;
import com.vitorsaucedo.vbank.entities.enums.TransactionType;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public Transaction toEntity(PixTransferRequest request, BankAccount payer, BankAccount payee) {
        Transaction transaction = new Transaction();
        transaction.setPayer(payer);
        transaction.setPayee(payee);
        transaction.setAmount(request.amount());
        transaction.setDescription(request.description());
        transaction.setType(TransactionType.PIX);
        transaction.setStatus(TransactionStatus.COMPLETED); // Status inicial ao salvar
        return transaction;
    }

    public TransactionResponse toResponse(Transaction t) {
        return new TransactionResponse(
                t.getId(),
                t.getAmount(),
                t.getCreatedAt(),
                t.getDescription(),
                t.getStatus(),
                mapAccountInfo(t.getPayer()),
                mapAccountInfo(t.getPayee())
        );
    }

    public StatementItemResponse toStatementItem(Transaction t, BankAccount currentAccount) {
        boolean isPayer = t.getPayer().getId().equals(currentAccount.getId());

        return new StatementItemResponse(
                t.getId(),
                t.getAmount(),
                t.getCreatedAt(),
                t.getDescription(),
                isPayer ? t.getPayee().getUser().getFullName() : t.getPayer().getUser().getFullName(),
                t.getType(),
                isPayer ? TransactionDirection.OUTBOUND : TransactionDirection.INBOUND
        );
    }

    private TransactionResponse.AccountInfo mapAccountInfo(BankAccount account) {
        return new TransactionResponse.AccountInfo(
                account.getUser().getFullName(),
                account.getUser().getDocument(),
                "Vbank",
                account.getAgency(),
                account.getAccountNumber()
        );
    }
}
