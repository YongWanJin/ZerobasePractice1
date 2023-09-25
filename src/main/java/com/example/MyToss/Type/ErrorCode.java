package com.example.MyToss.Type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNKNOWN_ERROR("내부 서버 오류 또는 알려지지 않은 오류가 발생했습니다."),
    USER_NOT_FOUND("사용자가 없습니다."),
    ACCOUNT_NOT_FOUND("계좌가 없습니다."),
    TRANSACTIONID_NOT_FOUND("해당 거래를 찾을 수 없습니다."),
    ACCOUNT_TRANSACTION_LOCK("해당 계좌는 현재 사용중입니다."),
    USER_ACCOUNT_UNMATCH("사용자와 계좌의 소유주가 다릅니다."),
    ACCOUNT_ALREADY_UNREGISTERED("이미 해지된 계좌입니다."),
    BALANCE_NOT_EMPTY("잔액이 남아있는 계좌는 해지할 수 없습니다."),
    MAX_ACCOUNT_PER_USER_10("계좌가 없습니다."),
    AMOUNT_EXCEED_BALANCE("거래 금액이 계좌 잔액보다 큽니다."),
    TRANSECTION_UNMATCH("이 거래는 해당 계좌에서 발생한 거래가 아닙니다."),
    CANCEL_MUST_FULLY("부분 취소는 허용되지 않습니다."),
    TOO_OLDER_TO_CANCEL("1년 이상 지난 거래는 취소가 불가능합니다."),
    INVALID_ID_REQUEST("잘못된 요청입니다.")
    ;

    private final String description;


}
