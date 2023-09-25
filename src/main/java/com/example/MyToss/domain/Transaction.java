package com.example.MyToss.domain;

import com.example.MyToss.Type.TransactionResultType;
import com.example.MyToss.Type.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Transaction extends BaseEntity{

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    @Enumerated(EnumType.STRING)
    private TransactionResultType transactionResultType;

    @ManyToOne
    private Account account; // 계좌 하나에서 여러 건의 거래가 얼마든지 발생가능
    private Long amount; // 거래 금액
    private Long balanceSnapshot; // 거래 후 잔액

    private String transactionId; // 거래 고유번호
    private LocalDateTime transactedAt; //

}
