package com.rewards.rewardscalculator.dto;
/*
 *Author : Sagar Enagaluru
 */

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class TransactionRequest {
    private static final long serialVersionUID = -4465133318181859565L;

    @NotNull(message = "itemName shouldn't be null")
    @NotEmpty(message = "itemName shouldn't be empty")
    private String itemName;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @NotNull(message = "transactionDate cannot be null")
    private LocalDate transactionDate;

    @Positive(message="totalAmount must be greater than 0")
    private Double totalAmount;

    @Positive(message = "customerId shouldn't be empty")
    private Integer customerId;
}
