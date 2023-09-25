package com.example.MyToss.service;


import com.example.MyToss.AOP.AccountLockIdInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class LockAopAspect {
    private final LockService lockService;
    // 어떤 경우에 Aspect를 적용할 것인가?
    @Around("@annotation(com.example.MyToss.AOP.AccountLock) && args(request)")
    public Object arountMethod(
            ProceedingJoinPoint pjp, // 현재 진행중인 join point
            AccountLockIdInterface request
                // @AccountLock하에 놓인 메서드의 request객체를 가져옴 (자료형 통일)
    ) throws Throwable
    {
        // lock 취득 시도
        lockService.lock(request.getAccountNumber());
        try{
            return pjp.proceed();
        } finally {
            // lock 해제
            lockService.unlock(request.getAccountNumber());
        }
    }
}
