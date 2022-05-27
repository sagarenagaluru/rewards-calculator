package com.rewards.rewardscalculator.util;
/*
 *Author : Sagar Enagaluru
 */

import com.rewards.rewardscalculator.entity.TransactionsEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class RewardsUtilTest {
    private List<TransactionsEntity> transactionsEntityList;

    @BeforeEach
    private void setUp() {
        TransactionsEntity transactionsEntity_1 = new TransactionsEntity();
        transactionsEntity_1.setItemName("Jeans");
        transactionsEntity_1.setCustomerId(1);
        transactionsEntity_1.setTotalAmount(293.59);
        transactionsEntity_1.setTransactionDate(LocalDate.of(2021, 01, 20));

        TransactionsEntity transactionsEntity_2 = new TransactionsEntity();
        transactionsEntity_2.setItemName("Shoes");
        transactionsEntity_2.setCustomerId(1);
        transactionsEntity_2.setTotalAmount(120.2);
        transactionsEntity_2.setTransactionDate(LocalDate.of(2021, 03, 30));
        transactionsEntityList = Stream.of(transactionsEntity_1, transactionsEntity_2).collect(Collectors.toList());
    }

    @Test
    public void testRewardsCalculation() {
        Assertions.assertEquals(90, RewardsUtil.calculateRewards(120d));
    }

    @Test
    public void testNagativeTransactionAmount() {
        Assertions.assertEquals(0, RewardsUtil.calculateRewards(-120d));
    }

    @Test
    public void testForTransactionAmount_70() {
        Assertions.assertEquals(20, RewardsUtil.calculateRewards(70d));
    }

    @Test
    public void testForTransactionAmountWithDecimal() {
        Assertions.assertEquals(92, RewardsUtil.calculateRewards(120.6));
    }

    @Test
    public void testForTransactionAmountWithPositiveInfinity() {
        Assertions.assertEquals(0, RewardsUtil.calculateRewards(Double.POSITIVE_INFINITY));
    }

    @Test
    public void testForTransactionAmountWithNegativeInfinity() {
        Assertions.assertEquals(0, RewardsUtil.calculateRewards(Double.NEGATIVE_INFINITY));
    }

    @Test
    public void testForTransactionAmountWithNaN() {
        Assertions.assertEquals(0, RewardsUtil.calculateRewards(Double.NaN));
    }


    @Test
    public void testThreeMonthsSummaryFromPrepareRewardsSummary() {
        Assertions.assertIterableEquals(transactionsEntityList,
                (RewardsUtil.prepareRewardsSummary(transactionsEntityList).getTransactionsList()));
    }

    @Test
    public void testTotalMonthsCounttFromPrepareRewardsSummary() {
        Assertions.assertEquals(2,
                RewardsUtil.prepareRewardsSummary(transactionsEntityList).getPointsByMonth().size());
    }

    @Test
    public void testPrepareRewardsSummaryWithEmptyTnxList() {
        List<TransactionsEntity> transactionsEntityList = new ArrayList<>();
        Assertions.assertNull(RewardsUtil.prepareRewardsSummary(transactionsEntityList));
    }
}
