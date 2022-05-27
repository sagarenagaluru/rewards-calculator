package com.rewards.rewardscalculator.service;
/*
 *Author : Sagar Enagaluru
 */

import com.rewards.rewardscalculator.advice.customExceptions.NoTransactionFoundException;
import com.rewards.rewardscalculator.dto.RewardsSummary;
import com.rewards.rewardscalculator.dto.TransactionRequest;
import com.rewards.rewardscalculator.entity.TransactionsEntity;
import com.rewards.rewardscalculator.repository.TransactionRepository;
import com.rewards.rewardscalculator.util.RewardsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.List;

@Service
public class TransactionsService {

    private Logger logger = LoggerFactory.getLogger(TransactionsService.class);

    @Autowired
    private TransactionRepository tnxRepository;

    public RewardsSummary calculatePointsForCustomer(int customerId) throws Exception {
        RewardsSummary rewardsSummary;
        logger.debug("executing query for rewardsSummary with customerId");
        List<TransactionsEntity> transactionsEntityList = tnxRepository.findAllByCustomerIdOrderByTransactionDateAsc(customerId);
        if (null != transactionsEntityList && !transactionsEntityList.isEmpty())
            rewardsSummary = RewardsUtil.prepareRewardsSummary(transactionsEntityList);
        else {
            logger.info("No rewardsSummary found with customerId {}", customerId);
            throw new NoTransactionFoundException("No rewardsSummary found");
        }
        return rewardsSummary;
    }


    public TransactionsEntity saveTransactionEntity(TransactionRequest transactionRequest) throws Exception {
        logger.info("Saving Transaction Request details into Database");
        if (transactionRequest.getItemName() == null || transactionRequest.getTransactionDate() == null ||
                transactionRequest.getTotalAmount() <= 0d || transactionRequest.getCustomerId() <= 0) {
            throw new InvalidParameterException("Invalid parameter value");
        }

        TransactionsEntity transactionEntity = TransactionsEntity.buildTransaction(
                transactionRequest.getItemName(),
                transactionRequest.getTransactionDate(),
                transactionRequest.getTotalAmount(),
                transactionRequest.getCustomerId());
        return tnxRepository.save(transactionEntity);
    }
}


