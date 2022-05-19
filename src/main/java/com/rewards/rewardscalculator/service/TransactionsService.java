package com.rewards.rewardscalculator.service;
/*
 *Author : Sagar Enagaluru
 */

import com.rewards.rewardscalculator.advice.customExceptions.NoTransactionFoundException;
import com.rewards.rewardscalculator.dto.RewardsSummary;
import com.rewards.rewardscalculator.dto.TransactionRequest;
import com.rewards.rewardscalculator.entity.TransactionsEntity;
import com.rewards.rewardscalculator.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransactionsService {

    private Logger logger = LoggerFactory.getLogger(TransactionsService.class);

    @Autowired
    private TransactionRepository tnxRepository;

    public RewardsSummary calculatePointsForCustomer(int customerId) throws Exception{
        RewardsSummary rewardsSummary;
        logger.debug("executing query for rewardsSummary with customerId");
        List<TransactionsEntity> transactionsEntityList = tnxRepository.findAllByCustomerIdOrderByTransactionDateAsc(customerId);
        if(null != transactionsEntityList && !transactionsEntityList.isEmpty())
            rewardsSummary = prepareRewardsSummary(transactionsEntityList);
        else {
            logger.info("No rewardsSummary found with customerId {}", customerId);
            throw new NoTransactionFoundException("No rewardsSummary found");
        }
        return rewardsSummary;
    }

    private RewardsSummary prepareRewardsSummary(List<TransactionsEntity> transactionsEntityList){
        logger.debug("Inside preparing rewards summary");
        List<TransactionsEntity> txnEntityListWithRewards = transactionsEntityList.stream().map(tnxEntity -> {
            Integer tnxAmount = (int)Math.round(tnxEntity.getTotalAmount());
            if(tnxAmount > 50 && tnxAmount <= 100)
                tnxEntity.setPoints(tnxAmount-50);
            else if(tnxAmount > 100)
                tnxEntity.setPoints(2*(tnxAmount-100) + 50);

            logger.info("Calculated {} rewards on transaction amount {}", tnxEntity.getPoints() , tnxEntity.getTotalAmount());

            return tnxEntity;
        }).collect(Collectors.toList());


        Map<String, Integer> pointsByMonth = txnEntityListWithRewards.stream()
                .collect(Collectors.groupingBy(tnxEntity -> tnxEntity.getTransactionDate().getMonth().name(),
                        LinkedHashMap::new,
                        Collectors.summingInt(TransactionsEntity::getPoints)));
        if(logger.isInfoEnabled())
            pointsByMonth.forEach((month,points)-> logger.info(month +" : "+ points));

        Integer totalRewardPoints = txnEntityListWithRewards.stream().map(TransactionsEntity::getPoints)
                .reduce(0, (integer, integer2) -> integer + integer2);
        logger.info("Total rewards {}",totalRewardPoints);

        //Logic for filtering last 3 months transaction
        LocalDate filterDate = (txnEntityListWithRewards.get(txnEntityListWithRewards.size()-1)).getTransactionDate().minusMonths(3);
        List<TransactionsEntity> tnxsForThreeMonths = txnEntityListWithRewards.stream().filter(transactionsEntity ->
                transactionsEntity.getTransactionDate().isAfter(filterDate)
        ).collect(Collectors.toList());

        return RewardsSummary.build(totalRewardPoints,pointsByMonth,tnxsForThreeMonths);
    }

    public TransactionsEntity saveTransactionEntity(TransactionRequest transactionRequest){
        logger.info("Saving Transaction Request details into Database");
        TransactionsEntity transactionEntity = TransactionsEntity.buildTransaction(
                transactionRequest.getItemName(),
                transactionRequest.getTransactionDate(),
                transactionRequest.getTotalAmount(),
                transactionRequest.getCustomerId());
        return tnxRepository.save(transactionEntity);
    }
}


