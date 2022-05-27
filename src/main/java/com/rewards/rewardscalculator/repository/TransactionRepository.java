package com.rewards.rewardscalculator.repository;
/*
 *Author : Sagar Enagaluru
 */

import com.rewards.rewardscalculator.entity.TransactionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionsEntity, Integer> {

    List<TransactionsEntity> findAllByCustomerIdAndTransactionDateBetweenOrderByTransactionDateAsc
            (int customerId, LocalDate transactionDateStart, LocalDate transactionDateEnd);

    List<TransactionsEntity> findAllByCustomerIdOrderByTransactionDateAsc(int customerId);
}
