package com.example.MyToss.service;

import com.example.MyToss.Type.ErrorCode;
import com.example.MyToss.exception.AccountException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class LockService {
    // config.RedisRepositoryConfig에 들어있는 메소드 redissonClient를 통해
    // 생성자를 자동으로 구현함. (annotation @RequiredArgsConstructor덕분에 가능)
    private final RedissonClient redissonClient;

    // lock에 접근하기 위한 메서드
    public void lock(String accountNumber) {
        // 계좌번호 자체를 Lock에 접근하기위한 key로 사용
        RLock lock = redissonClient.getLock(getLockKey(accountNumber));
        log.debug("Trying lock for accountNumber : {}", accountNumber);

        try{
            // 최대 1초동안 lock에 접근 시도,
            // 10초 후에 자동으로 lock 해제.
            // (이 10초 동안에는 다른 곳에서 lock에 접근할 수 없음. 동시성 해결)
            boolean isLock = lock.tryLock(1, 15, TimeUnit.SECONDS);

            // 자물쇠 획득 실패 : 이미 사용중인 lock에 접근 시도
            if(!isLock){
                log.error("===== Lock acquisition failed =====");
                throw new AccountException(ErrorCode.ACCOUNT_TRANSACTION_LOCK);
            }
            // 그 외 이유로 자물쇠 획득 실패
        } catch (Exception e){
            log.error("=========Redis lock failed==========");
        }
    }

    // key를 획득하기 위한 메서드
    private String getLockKey(String accountNumber) {
            return "ACLK:" + accountNumber;
    }

    // lock을 해제하기 위한 메서드
    public void unlock(String accountNumber){
        log.debug("Unlock for accountNumber : {}", accountNumber);
        redissonClient.getLock(getLockKey(accountNumber)).unlock();
    }
}