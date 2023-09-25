package com.example.MyToss.controller;


import com.example.MyToss.dto.AccountDto;
import com.example.MyToss.dto.AccountInfo;
import com.example.MyToss.dto.CreateAccount;
import com.example.MyToss.dto.DeleteAccount;
import com.example.MyToss.service.AccountService;
import com.example.MyToss.service.LockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController  // 이 컨트롤러를 bean으로 등록
@RequiredArgsConstructor
// # 데이터 흐름 플로우
// : 1)클라이언트(url 요청) -> 2)컨트롤러(AccountController)
// -> 3)서비스(AccountService) -> 4)리포지토리(AccountRepository)

// * 클라이언트는 오직 컨트롤러에만 접근할 수 있다.
public class AccountController {
    private final AccountService accountService; // 서비스에 접속하는 객체
    private final LockService lockService; // redisson 접속을 위한 객체

//    // # redisson 접속 (접근 권한 획득)
//    @GetMapping("/get-lock")
//    public String getLock(){
//        return lockService.lock();
//    }


    // # 신규 계좌 생성
    @PostMapping("/account") // 사용자가 해당 url을 입력하면 (Post 매서드 방식)
    public CreateAccount.Response createAccount(
            @RequestBody @Valid CreateAccount.Request request
            )
    {
        AccountDto accountDto = accountService.createAccount(
                request.getUserId(), request.getInitialBalance());
        return CreateAccount.Response.from(accountDto);
    }

    // # 계좌 해지
    @PostMapping("/accountDelete")
    public DeleteAccount.Response deleteAccount(@RequestBody @Valid DeleteAccount.Request request) {
        AccountDto accountDto = accountService.deleteAccount(
                // ** 이 부분 매서드 delete로 고칠것
                request.getUserId(), request.getAccountNumber());
        return DeleteAccount.Response.from(accountDto);
    }

//    // # id를 통한 계좌 조회
//    @GetMapping("/account/{id}")
//    public Account getAccount(@PathVariable Long id){
//        return accountService.getAccount(id);
//    }

    // # 계좌 조회
    @GetMapping("/get-account")
    public List<AccountInfo> getAccountsByUserId(@RequestParam("user_id") Long userId){
        accountService.getAccountsByUserId(userId);

        return accountService.getAccountsByUserId(userId)
                .stream().map(accountDto -> AccountInfo.builder()
                        .accountNumber(accountDto.getAccountNumber())
                        .balance(accountDto.getBalance())
                        .build())
                .collect(Collectors.toList());
    }
}
