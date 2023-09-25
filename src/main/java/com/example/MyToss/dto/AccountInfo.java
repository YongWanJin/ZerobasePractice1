package com.example.MyToss.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// 클라이언트와 애플리케이션(컨트롤러, AccountController) 사이에서 주고받는 계좌 정보.
// AccountDto와 다르게, 이 AccountInfo는 필요한 정보만 골라서 제공할 수 있다.

// AccountInfo, 왜 필요한가? AccountDto에서 그냥 정보를 빼오면 되지 않나?
// 하나의 클래스가 여러가지의 용도로 쓰인다면, 예기치못한 에러가 날 가능성이 높아진다.

public class AccountInfo{
    // 이 클래스는 계좌번호와 잔액만 보여주는 용도로 쓰인다.
    private String accountNumber;
    private Long balance;
}
