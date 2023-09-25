package com.example.MyToss.service;

import com.example.MyToss.Type.AccountStatus;
import com.example.MyToss.Type.ErrorCode;
import com.example.MyToss.domain.Account;
import com.example.MyToss.domain.AccountUser;
import com.example.MyToss.dto.AccountDto;
import com.example.MyToss.exception.AccountException;
import com.example.MyToss.repository.AccountRepository;
import com.example.MyToss.repository.AccountUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.MyToss.Type.AccountStatus.IN_USE;
import static com.example.MyToss.Type.ErrorCode.*;

@Service // Service타입 bean
@RequiredArgsConstructor  // 생성자를 입력한 객체에따라 알아서 만들어주는 annotation
public class AccountService{
    private final AccountRepository accountRepository; // 생성자만 받을수 있는(final) 변수
    private final AccountUserRepository accountUserRepository;

    // # Account 테이블에 데이터 insert
    /**
     * 1. 사용자가 있는지 조회
     * 2. 계좌 번호 생성
     * 3. 계좌를 저장하고, 그 정보를 넘긴다.
     * */
    @Transactional
    public AccountDto createAccount(Long userId, Long initialBalance){
        // 1. 사용자가 있는지 조회
        AccountUser accountUser = accountUserRepository.findById(userId)
                .orElseThrow( ()->new AccountException(USER_NOT_FOUND) );
            // orElseThrow() : 만약 존재하지 않으면 다음과같은 예외를 발생시켜라

        // 2. 계좌번호 생성
        String newAccountNumber = accountRepository.findFirstByOrderByIdDesc()
                .map(account -> (Integer.parseInt(account.getAccountNumber())) + 1 + "")
                .orElse("10000000");

        // 3. 계좌를 저장하고 그 정보를 리턴한다.
        Account savedAccount = accountRepository.save(
                Account.builder()
                        .accountUser(accountUser)
                        .accountStatus(IN_USE)
                        .accountNumber(newAccountNumber)
                        .balance(initialBalance)
                        .registeredAt(LocalDateTime.now())
                        .build()
        );
        return AccountDto.fromEntity(savedAccount);

    }

    // # Account 테이블로부터 데이터 가져오기
    @Transactional
    public Account getAccount(Long id){
        return accountRepository.findById(id).get();
    }


    // # Account 삭제(계좌 해지)하기
    @Transactional
    public AccountDto deleteAccount(Long userId, String accountNumber){
        // 1. 사용자가 있는지 조회
        AccountUser accountUser = accountUserRepository.findById(userId)
                .orElseThrow( ()->new AccountException(USER_NOT_FOUND) );

        // 2. 계좌가 있는지 조회
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow( ()->new AccountException(ACCOUNT_NOT_FOUND) );

        // 3. 지울 수 있는 계좌인지 검증
        validateDeleteAccount(accountUser, account);

        // 4. 계좌 상태를 '해지'로 변경
        account.setAccountStatus(AccountStatus.UNREGISTERED);

        // 5. 마지막 수정 시간 갱신
        account.setUnRegisteredAt(LocalDateTime.now());

        return AccountDto.fromEntity(account);
    }

    private void validateDeleteAccount(AccountUser accountUser, Account account) {
        if(accountUser.getId().longValue() != account.getAccountUser().getId().longValue()){
            throw new AccountException(USER_ACCOUNT_UNMATCH);
        }
        if(account.getAccountStatus() == AccountStatus.UNREGISTERED){
            throw new AccountException(ACCOUNT_ALREADY_UNREGISTERED);
        }
        if(account.getBalance() > 0){
            throw new AccountException(BALANCE_NOT_EMPTY);
        }
    }


    // # 계좌 확인하기 (계좌번호, 잔액)
    public List<AccountDto> getAccountsByUserId(Long userId) {
        AccountUser accountUser = accountUserRepository.findById(userId)
                .orElseThrow( () -> new AccountException(USER_NOT_FOUND) );
        List<Account> accounts = accountRepository.findByAccountUser(accountUser);

        return accounts.stream()
                .map(AccountDto::fromEntity)
                .collect(Collectors.toList());
    }
}

