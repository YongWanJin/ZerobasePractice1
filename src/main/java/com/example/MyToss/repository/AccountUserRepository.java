package com.example.MyToss.repository;

import com.example.MyToss.domain.Account;
import com.example.MyToss.domain.AccountUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.stereotype.Repository;

@Repository
@RedisHash
public interface AccountUserRepository extends JpaRepository<AccountUser, Long> {
}
