package com.example.MyToss.dto;

import com.example.MyToss.Type.TransactionResultType;
import com.example.MyToss.Type.TransactionType;
import com.example.MyToss.domain.Account;
import com.example.MyToss.domain.Transaction;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDto {
    private String accountNumber; // 계좌번호
    private TransactionType transactionType;
    private TransactionResultType transactionResultType;
    private Long amount; // 거래 금액
    private Long balanceSnapshot; // 거래 후 잔액
    private String transactionId; // 거래 고유번호
    private LocalDateTime transactedAt; //

    // Domain -> DTO
    public static TransactionDto fromEntity(Transaction transaction){
        return TransactionDto.builder()
                .accountNumber(transaction.getAccount().getAccountNumber())
                .transactionType(transaction.getTransactionType())
                .transactionResultType(transaction.getTransactionResultType())
                .amount(transaction.getAmount())
                .balanceSnapshot(transaction.getBalanceSnapshot())
                .transactionId(transaction.getTransactionId())
                .transactedAt(transaction.getTransactedAt())
                .build();
    }
}
