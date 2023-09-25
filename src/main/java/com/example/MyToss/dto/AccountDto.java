package com.example.MyToss.dto;

import com.example.MyToss.domain.Account;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

// ./domain/Account의 DTO(Data Transfer Object)버전.
// domain에 속하는 Account는 비즈니스 로직을 담는 역할(Busniess Layer)이다.
// DTO에 속하는 AccountDto는 데이터를 전달하는 역할(Presentation Layer)이며,
// 컨트롤러(AccountController)와 서비스(AccountService)간 계좌 정보를 주고받는 용도

// 용도와 자료형이 다를 뿐,
// 근본적으로 Account와 AccountDto이 담고 있는 내용은 동일하다.
// https://e-una.tistory.com/72

// DB에 직접 저장되는 Entity타입 객체(./domain/Account)와는 다르게,
// 내가 원하는 속성만 골라낼 수 있다.
public class AccountDto {
    private Long userId;
    private String userName;
    private String accountNumber;
    private Long balance;

    private LocalDateTime registeredAt;
    private LocalDateTime unRegisteredAt;

    // 엔티티(Account) -> 일반 클래스(AccountDto) 변환 메서드
    public static AccountDto fromEntity(Account account){
        return AccountDto.builder()
                .userId(account.getAccountUser().getId())
                .userName(account.getAccountUser().getName())
                .accountNumber(account.getAccountNumber())
                .registeredAt(account.getRegisteredAt())
                .unRegisteredAt(account.getUnRegisteredAt())
                .build();
    }
}
