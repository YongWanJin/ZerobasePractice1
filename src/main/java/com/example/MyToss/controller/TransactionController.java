package com.example.MyToss.controller;

import com.example.MyToss.AOP.AccountLock;
import com.example.MyToss.dto.CancelBalance;
import com.example.MyToss.dto.QueryTransactionResponse;
import com.example.MyToss.dto.TransactionDto;
import com.example.MyToss.dto.UseBalance;
import com.example.MyToss.exception.AccountException;
import com.example.MyToss.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 잔액 관련 컨트롤러
 * 1. 잔액 사용
 * 2. 잔액 사용 취소
 * 3. 거래(transaction, 매매) 확인
 * */
@Slf4j
@RestController
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    // # 잔액 사용하기
    @PostMapping("/transaction/use")
    @AccountLock // 동시성을 해결하기 위한 코드가 동작하도록 하는 어노테이션
    public UseBalance.Response useBalance (
            @Valid @RequestBody UseBalance.Request request)
    {
        try{
            Thread.sleep(5000L);
            TransactionDto transactionDto = transactionService.userBalance(
                    request.getUserId(), request.getAccountNumber(), request.getAmount());
            return UseBalance.Response.from(transactionDto);

        } catch(AccountException e){
            log.error("잔액 사용 실패");
            transactionService.saveFailedUseTransaction(
                    request.getAccountNumber(), request.getAmount()
            );
            throw e;

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // # 잔액 사용 취소하기(환뷸, 결제 취소)
    @PostMapping("/transaction/cancel")
    @AccountLock
    public CancelBalance.Response cancelBalance(
            @Valid @RequestBody CancelBalance.Request request)
    {
        try{
            TransactionDto transactionDto = transactionService.cancelBalance(
                    request.getTransactionId(), request.getAccountNumber(), request.getAmount());
            return CancelBalance.Response.from(transactionDto);
        } catch(AccountException e){
            log.error("잔액 사용 실패");
            transactionService.saveFailedUseTransaction(
                    request.getAccountNumber(), request.getAmount()
            );
            throw e;
        }
    }

    // # 거래 내역 확인하기
    @GetMapping("/transaction/{transactionId}")
    public QueryTransactionResponse queryTransaction(
            @PathVariable String transactionId){
        return QueryTransactionResponse.from(
                transactionService.queryTransaction(transactionId));
    }


}
