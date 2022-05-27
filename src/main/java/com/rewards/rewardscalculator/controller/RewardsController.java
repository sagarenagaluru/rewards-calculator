package com.rewards.rewardscalculator.controller;
/*
 *Author : Sagar Enagaluru
 */

import com.rewards.rewardscalculator.dto.RewardsSummary;
import com.rewards.rewardscalculator.dto.TransactionRequest;
import com.rewards.rewardscalculator.entity.TransactionsEntity;
import com.rewards.rewardscalculator.service.TransactionsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/customers")
public class RewardsController {

    @Autowired
    private TransactionsService transactionService;

    private Logger logger = LoggerFactory.getLogger(TransactionsService.class);

    @GetMapping(value = "/{id}/calculate")
    private ResponseEntity<RewardsSummary> calculateRewardPointsWithCustomerId(
            @PathVariable("id") int customerId) throws Exception {
        logger.info("Inside controller to calculate rewards with CustomerID :{}", customerId);
        return ResponseEntity.ok(transactionService.calculatePointsForCustomer(customerId));
    }

    @PostMapping("/saveTransaction")
    private ResponseEntity<TransactionsEntity> saveTransaction(@RequestBody @Valid TransactionRequest transactionRequest) throws Exception {
        return new ResponseEntity<>(transactionService.saveTransactionEntity(transactionRequest), HttpStatus.CREATED);
    }
}
