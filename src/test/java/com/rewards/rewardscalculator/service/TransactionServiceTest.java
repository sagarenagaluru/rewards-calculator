package com.rewards.rewardscalculator.service;
/*
 *Author : Sagar Enagaluru
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rewards.rewardscalculator.advice.customExceptions.NoTransactionFoundException;
import com.rewards.rewardscalculator.dto.TransactionRequest;
import com.rewards.rewardscalculator.entity.TransactionsEntity;
import com.rewards.rewardscalculator.repository.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;
    private List<TransactionsEntity> transactionsEntityListWithCustomerId_1;
    private List<TransactionsEntity> transactionsEntityListWithCustomerId_2;

    @InjectMocks
    private TransactionsService transactionsService;

    private ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    public void setUp() {
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
        transactionsEntityListWithCustomerId_1 = Stream.of(transactionsEntity_1, transactionsEntity_2).collect(Collectors.toList());

        TransactionsEntity transactionsEntity_3 = new TransactionsEntity();
        transactionsEntity_3.setCustomerId(2);
        transactionsEntityListWithCustomerId_2 = Stream.of(transactionsEntity_3).collect(Collectors.toList());


    }

    @Test
    public void testRewardsSummaryWithCustomerId() throws Exception {
        Mockito.when(transactionRepository.findAllByCustomerIdOrderByTransactionDateAsc(1)).thenReturn(transactionsEntityListWithCustomerId_1);
        Assertions.assertEquals(528, (transactionsService.calculatePointsForCustomer(1).getTotalRewardPoints().longValue()));
    }

    @Test
    public void testRewardsSummaryWithEmptyList() throws Exception {
        Mockito.when(transactionRepository.findAllByCustomerIdOrderByTransactionDateAsc(1)).thenReturn(new ArrayList<TransactionsEntity>());
        NoTransactionFoundException exception = Assertions.assertThrowsExactly(NoTransactionFoundException.class, () -> {
            transactionsService.calculatePointsForCustomer(1);
        });
    }

    @Test
    public void testRewardsSummaryWithNullData() {
        Mockito.when(transactionRepository.findAllByCustomerIdOrderByTransactionDateAsc(2)).thenReturn(transactionsEntityListWithCustomerId_2);
        Assertions.assertThrows(NullPointerException.class, () -> transactionsService.calculatePointsForCustomer(2));
    }

    @Test
    public void testSaveTransactionWithItemNameAsNull() {
        TransactionRequest transactionRequest = TransactionRequest.build(null, LocalDate.of(2022, 04, 20), 98.78, 1);
        Assertions.assertThrows(InvalidParameterException.class, () -> {
            transactionsService.saveTransactionEntity(transactionRequest);
        });
    }

    @Test
    public void testSaveTransactionWithDateAsNull() throws Exception {
        TransactionRequest transactionRequest = TransactionRequest.build("Shoes", null, 98.78, 1);
        Assertions.assertThrows(InvalidParameterException.class, () -> {
            transactionsService.saveTransactionEntity(transactionRequest);
        });
    }

    @Test
    public void testSaveTransactionWithTotalAmountAsZero() throws Exception {
        TransactionRequest transactionRequest = TransactionRequest.build("Shoes", null, 0d, 1);
        Assertions.assertThrows(InvalidParameterException.class, () -> {
            transactionsService.saveTransactionEntity(transactionRequest);
        });
    }
}
