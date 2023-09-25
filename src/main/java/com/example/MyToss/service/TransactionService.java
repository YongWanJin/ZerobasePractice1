package com.example.MyToss.service;

import com.example.MyToss.Type.AccountStatus;
import com.example.MyToss.Type.ErrorCode;
import com.example.MyToss.Type.TransactionResultType;
import com.example.MyToss.Type.TransactionType;
import com.example.MyToss.domain.Account;
import com.example.MyToss.domain.AccountUser;
import com.example.MyToss.domain.Transaction;
import com.example.MyToss.dto.TransactionDto;
import com.example.MyToss.exception.AccountException;
import com.example.MyToss.repository.AccountRepository;
import com.example.MyToss.repository.AccountUserRepository;
import com.example.MyToss.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import static com.example.MyToss.Type.TransactionResultType.FAIL;
import static com.example.MyToss.Type.TransactionResultType.SUCCESS;
import static com.example.MyToss.Type.TransactionType.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountUserRepository accountUserRepository;
    private final AccountRepository accountRepository;


    // # 거래 개시
    /**
     * 거래가 안되는 경우들 :
     * 1) 사용자 없는 경우, 2) 사용자 아이디와 계좌 소유주가 다른 경우,
     * 3) 계좌가 이미 해지 상태인 경우, 4) 거래금액이 잔액보다 큰 경우,
     * 5) 거래금액이 너무 작거나 큰 경우, 6) 계좌가 없는 경우
     * */
    @Transactional
    public TransactionDto userBalance(Long userId, String accountNumber, Long amount){

        AccountUser user = accountUserRepository.findById(userId)
                .orElseThrow(() -> new AccountException((ErrorCode.USER_NOT_FOUND)));

        Account account = accountRepository.findByAccountNumber(accountNumber)
                        .orElseThrow(() -> new AccountException(ErrorCode.ACCOUNT_NOT_FOUND));
        validatedUseBalance(user, account, amount);

        // * 거래 후 잔액 계산
        account.useBalance(amount);

        // * 매매 결과를 레포지토리에 저장
        Transaction transaction = getTransaction(USE, SUCCESS, account, amount);

        // * 매매 결과를 DTO로 변환
        return TransactionDto.fromEntity(transaction);
    }

    // # 올바른 거래인지 검증하는 메서드
    private void validatedUseBalance(AccountUser user, Account account, Long amount) {
        if( !Objects.equals(user.getId(), account.getAccountUser().getId()) ){
            throw new AccountException(ErrorCode.USER_ACCOUNT_UNMATCH);
        }
        if( account.getAccountStatus() != AccountStatus.IN_USE ){
            throw new AccountException(ErrorCode.ACCOUNT_ALREADY_UNREGISTERED);
        }
        if(account.getBalance() < amount){
            throw new AccountException(ErrorCode.AMOUNT_EXCEED_BALANCE);
        }
    }

    @Transactional
    // # 거래 실패 예외처리가 발생한 로그를 저장하는 메서드
    public void saveFailedUseTransaction(String accountNumber, Long amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(()-> new AccountException(ErrorCode.ACCOUNT_NOT_FOUND));

        // * 매매 결과를 레포지토리에 저장
        Transaction transaction = getTransaction(USE, FAIL, account, amount);
    }

    // # 처리 결과를 리포지토리에 저장하는 메서드
    private Transaction getTransaction(
            TransactionType transactionType,
            TransactionResultType transactionResultType,
            Account account,
            Long amount) {
        Transaction transaction = transactionRepository.save(
                Transaction.builder()
                        .transactionType(transactionType)
                        .transactionResultType(transactionResultType)
                        .account(account)
                        .amount(amount)
                        .balanceSnapshot(account.getBalance())
                        .transactionId(UUID.randomUUID().toString().replace("-", ""))
                            // UUID : 고유 번호를 만들어주는 알고리즘의 일종.
                            // 널리 검증되었고, 편리하다고 한다.
                        .transactedAt(LocalDateTime.now())
                        .build()
        );
        return transaction;
    }

    // -------------------------------------------------------------------- //

    // # 거래 취소 메서드
    @Transactional
    public TransactionDto cancelBalance(String transectionId, String accountNumber, Long amount){
        // * transactionId로 취소할 거래 찾기
        Transaction transaction = transactionRepository.findByTransactionId(transectionId)
                .orElseThrow(() -> new AccountException(ErrorCode.TRANSACTIONID_NOT_FOUND));

        // * 취소 요청한 계좌
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountException(ErrorCode.ACCOUNT_NOT_FOUND));

        // * 취소가 가능한지 검증
        validateCancelBalance(transaction, account, amount);

        // * (검증 통과 후) 취소 진행
        account.cancelBalance(amount);

        // * 취소한 내역 저장
        Transaction transaction2 = getTransaction(CANCEL, SUCCESS, account, amount);

        // * 취소 결과를 Dto로 반환
        return TransactionDto.fromEntity(transaction2);


    }

    // # 취소가 가능한지 검증하는 메서드
    private void validateCancelBalance(Transaction transaction, Account account, Long amount) {
        // * 현재 계좌와 거래 내역의 계좌가 불일치
        if( !Objects.equals(transaction.getAccount().getId(), account.getId()) ){
            throw new AccountException(ErrorCode.TRANSECTION_UNMATCH);
        }
        // * 결제한 금액과 취소 신청한 금액이 서로 다를 경우
        if( (!Objects.equals(transaction.getAmount(), amount)) ){
            throw new AccountException(ErrorCode.CANCEL_MUST_FULLY);
        }
        // * 거래한 시각이 현재부터 1년 이전인 경우
        if(transaction.getTransactedAt().isBefore(LocalDateTime.now().minusYears(1))){
            throw new AccountException(ErrorCode.TOO_OLDER_TO_CANCEL);
        }
    }

    // -------------------------------------------------------------------- //

    // # 거래를 확인하는 메서드
    public TransactionDto queryTransaction(String transactionId) {
        // * transactionId로 확인할 거래 찾기
        Transaction transaction = transactionRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new AccountException(ErrorCode.TRANSACTIONID_NOT_FOUND));

        // * 확인 결과 리턴
        Transaction transaction3 = transactionRepository.findByTransactionId(transactionId)
                .orElseThrow(()-> new AccountException(ErrorCode.TRANSACTIONID_NOT_FOUND));
        return TransactionDto.fromEntity(transaction3);
    }
}
