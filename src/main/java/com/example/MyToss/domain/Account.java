package com.example.MyToss.domain;

import com.example.MyToss.Type.AccountStatus;
import com.example.MyToss.Type.ErrorCode;
import com.example.MyToss.exception.AccountException;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

// # Table Account 생성하기
// : 계좌 생성 API에서 꼭 필요한 컬럼들을 기입 (교안 참고)

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)

public class Account extends BaseEntity{

    @ManyToOne
    private AccountUser accountUser;
    private String accountNumber;

    @Enumerated(EnumType.STRING)  // 이 변수가 enum타입임을 알려줌
    private AccountStatus accountStatus;
    private Long balance;

    @CreatedDate
    private LocalDateTime registeredAt;

    @LastModifiedDate
    private LocalDateTime unRegisteredAt;


    // # 거래 후 잔액 계산같이 중요한 기능은
    // 직접 처리하도록 해야한다.
    public void useBalance(Long amount){
        if(amount > balance){
            throw new AccountException(ErrorCode.AMOUNT_EXCEED_BALANCE);
        }
        balance -= amount;
    }

    public void cancelBalance(Long amount){
        if(amount < 0){
            throw new AccountException(ErrorCode.INVALID_ID_REQUEST);
        }
        balance += amount;
    }


}