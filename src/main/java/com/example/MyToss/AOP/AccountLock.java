package com.example.MyToss.AOP;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited

// 동시성을 해결하기 위한 코드가 동작하도록 하는 어노테이션
public @interface AccountLock {
    long tryLockTime() default 5000L; // 5000ms동안 기다리겠다.
}
