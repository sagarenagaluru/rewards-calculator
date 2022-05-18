package com.rewards.rewardscalculator.service;
/*
 *Author : Sagar Enagaluru
 */

import com.rewards.rewardscalculator.dto.RewardsSummary;
import com.rewards.rewardscalculator.entity.TransactionsEntity;
import com.rewards.rewardscalculator.repository.TransactionRepository;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Equals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
public class TransactionServiceTest {

    @Autowired
    TransactionsService transactionsService;
    @MockBean
    TransactionRepository transactionRepository;
    List<TransactionsEntity> transactionsEntityList;

    @BeforeEach
    public void setUp(){
        TransactionsEntity transactionsEntity_1 = new TransactionsEntity();
        transactionsEntity_1.setItemName("Jeans");
        transactionsEntity_1.setCustomerId(1);
        transactionsEntity_1.setTotalAmount(293.59);
        transactionsEntity_1.setTransactionDate(LocalDate.of(2021,01,20));

        TransactionsEntity transactionsEntity_2 = new TransactionsEntity();
        transactionsEntity_2.setItemName("Shoes");
        transactionsEntity_2.setCustomerId(1);
        transactionsEntity_2.setTotalAmount(120.2);
        transactionsEntity_2.setTransactionDate(LocalDate.of(2021,03,30));
        transactionsEntityList = Stream.of(transactionsEntity_1,transactionsEntity_2).collect(Collectors.toList());
    }

    @Test
    public void testRewardsSummaryWithCustomerId(){
        Mockito.when(transactionRepository.findAllByCustomerIdOrderByTransactionDateAsc(1)).thenReturn(transactionsEntityList);
        Assert.assertEquals(528, (transactionsService.calculatePointsForCustomer(1).getTotalRewardPoints().longValue()));
    }
}
