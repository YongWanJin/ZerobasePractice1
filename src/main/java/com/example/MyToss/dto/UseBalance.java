package com.example.MyToss.dto;

import com.example.MyToss.AOP.AccountLockIdInterface;
import com.example.MyToss.Type.TransactionResultType;
import com.example.MyToss.Type.TransactionType;
import com.example.MyToss.domain.Account;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

public class UseBalance {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request implements AccountLockIdInterface {
        // AOP.AccountLockIdInterface : CancelBalance에 있는 Request와
        // 자료형을 통일시켜주기 위한 인터페이스

        @NotNull
        @Min(1)
        private Long userId;

        @NotBlank
        @Size(min = 8, max = 8)
        private String accountNumber;

        @NotNull
        @Min(10)
        @Max(1000_000_000)
        private Long amount; // 거래금액

        public String getAccountNumber(){
            return accountNumber;
        }
    }


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response{
        private String accountNumber;
        private TransactionResultType transactionResultType;
        private String transactionId;
        private Long amount;
        private LocalDateTime transactedAt;

        // # DTO -> Response객체
        public static Response from(TransactionDto transactionDto){
            return Response.builder()
                    .accountNumber(transactionDto.getAccountNumber())
                    .transactionResultType(transactionDto.getTransactionResultType())
                    .transactionId(transactionDto.getTransactionId())
                    .amount(transactionDto.getAmount())
                    .transactedAt(transactionDto.getTransactedAt())
                    .build();
        }
    }
}