package com.example.MyToss.AOP;

/** service.LockAopAspect에서 동시성 해결을 할 때에,
 *  controller.TransactionController의 두 메서드(어노테이션 @AccountLock이 걸려있는)
 *  useBalance(), cancelBalance()의 전달인자 request 객체를
 *  하나의 자료형으로 통일시켜주기 위한 인터페이스.
 *  request 객체의 자료형은 useBalance()메서드에서 UseBalance.Request로,
 *  cancelBalance()메서드에서 CancelBalance.Request로 서로 다르다.
 * */

public interface AccountLockIdInterface {
    String getAccountNumber();
}
