package com.example.MyToss.repository;

import com.example.MyToss.domain.Account;
import com.example.MyToss.domain.AccountUser;
import com.example.MyToss.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RedisHash
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByTransactionId(String transactionId);
}
