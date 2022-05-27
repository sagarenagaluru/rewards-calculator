package com.rewards.rewardscalculator.entity;
/*
 *Author : Sagar Enagaluru
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "TRANSACTIONS")
@Data
@RequiredArgsConstructor(staticName = "buildTransaction")
@NoArgsConstructor
public class TransactionsEntity implements Serializable {
    private static final long serialVersionUID = -3527522802429958568L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TRANSACTION_ID", nullable = false)
    @JsonProperty("transaction_id")
    private int transactionId;

    @Column(name = "ITEM_NAME", nullable = false)
    @JsonProperty("item_name")
    @NonNull
    private String itemName;

    @Column(name = "TRANSACTION_DATE", nullable = false)
    @JsonProperty("transaction_date")
    @NonNull
    private LocalDate transactionDate;

    @Column(name = "TRANSACTION_AMOUNT", columnDefinition = "double default 0", precision = 2)
    @JsonProperty("transaction_amount")
    @NonNull
    private Double totalAmount;

    @Column(name = "CUSTOMER_ID", nullable = false)
    @JsonProperty("customer_id")
    @NonNull
    private Integer customerId;

    @JsonProperty("reward_points")
    private Integer points = 0;

}
