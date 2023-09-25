package com.example.MyToss.repository;

import com.example.MyToss.domain.Account;
import com.example.MyToss.domain.AccountUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RedisHash
public interface AccountRepository extends JpaRepository<Account, Long> {
    // # JpaRepository<Account, Long>
    // 첫번째 자료형 : 이 Repository에서 사용할 엔티티 => Account
    // 두번째 자료형 : pk column에서 취하는 자료형 => id의 자료형은 Long

    Optional<Account> findFirstByOrderByIdDesc();

    Integer countByAccountUser(AccountUser accountUser);

    Optional<Account> findByAccountNumber(String accountNumber);

    List<Account> findByAccountUser(AccountUser accountUser);
}
