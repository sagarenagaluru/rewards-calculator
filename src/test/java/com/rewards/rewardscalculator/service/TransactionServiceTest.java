package com.rewards.rewardscalculator.service;
/*
 *Author : Sagar Enagaluru
 */

import com.rewards.rewardscalculator.entity.TransactionsEntity;
import com.rewards.rewardscalculator.repository.TransactionRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
class TransactionServiceTest {

    @Autowired
    private TransactionsService transactionsService;
    @MockBean
    private TransactionRepository transactionRepository;
    private List<TransactionsEntity> transactionsEntityList;

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
    public void testRewardsSummaryWithCustomerId() throws Exception{
        Mockito.when(transactionRepository.findAllByCustomerIdOrderByTransactionDateAsc(1)).thenReturn(transactionsEntityList);
        Assert.assertEquals(528, (transactionsService.calculatePointsForCustomer(1).getTotalRewardPoints().longValue()));
    }

}
