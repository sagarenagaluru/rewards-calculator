package com.rewards.rewardscalculator.util;
/*
 *Author : Sagar Enagaluru
 */

import com.rewards.rewardscalculator.dto.RewardsSummary;
import com.rewards.rewardscalculator.entity.TransactionsEntity;
import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class RewardsUtil {

    private Logger logger = LoggerFactory.getLogger(RewardsUtil.class);

    public int calculateRewards(Double tnxAmount) {
        int convertedTnxAmount = (int) Math.round(tnxAmount);
        int calculatedRewards = 0;
        if (convertedTnxAmount > 50 && convertedTnxAmount <= 100)
            calculatedRewards = convertedTnxAmount - 50;
        else if (convertedTnxAmount > 100)
            calculatedRewards = (2 * (convertedTnxAmount - 100) + 50);
        return calculatedRewards;
    }


    public RewardsSummary prepareRewardsSummary(List<TransactionsEntity> transactionsEntityList) {
        logger.debug("Inside preparing rewards summary");
        if (transactionsEntityList != null && transactionsEntityList.isEmpty()) {
            logger.info("Transaction list is empty or null");
            return null;
        }
        List<TransactionsEntity> txnEntityListWithRewards = transactionsEntityList.stream().map(tnxEntity -> {
            tnxEntity.setPoints(RewardsUtil.calculateRewards(tnxEntity.getTotalAmount()));
            logger.info("Calculated {} rewards on transaction amount {}", tnxEntity.getPoints(), tnxEntity.getTotalAmount());
            return tnxEntity;
        }).collect(Collectors.toList());

        Map<String, Integer> pointsByMonth = txnEntityListWithRewards.stream()
                .collect(Collectors.groupingBy(tnxEntity -> tnxEntity.getTransactionDate().getMonth().name(),
                        LinkedHashMap::new,
                        Collectors.summingInt(TransactionsEntity::getPoints)));
        if (logger.isInfoEnabled())
            pointsByMonth.forEach((month, points) -> logger.info(month + " : " + points));

        Integer totalRewardPoints = txnEntityListWithRewards.stream().map(TransactionsEntity::getPoints)
                .reduce(0, (integer, integer2) -> integer + integer2);
        logger.info("Total rewards {}", totalRewardPoints);

        //Logic for filtering last 3 months transaction
        LocalDate filterDate = (txnEntityListWithRewards.get(txnEntityListWithRewards.size() - 1)).getTransactionDate().minusMonths(3);
        List<TransactionsEntity> tnxsForThreeMonths = txnEntityListWithRewards.stream().filter(transactionsEntity ->
                transactionsEntity.getTransactionDate().isAfter(filterDate)
        ).collect(Collectors.toList());

        return RewardsSummary.build(totalRewardPoints, pointsByMonth, tnxsForThreeMonths);
    }
}
