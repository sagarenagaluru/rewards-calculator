package com.rewards.rewardscalculator.dto;
/*
 *Author : Sagar Enagaluru
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rewards.rewardscalculator.entity.TransactionsEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor(staticName = "build")
public class RewardsSummary {
    private Integer totalRewardPoints;
    private Map<String, Integer> pointsByMonth;
    @JsonProperty("last_three_months_transaction")
    private List<TransactionsEntity> transactionsList;
}
