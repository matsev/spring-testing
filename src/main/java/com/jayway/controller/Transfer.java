package com.jayway.controller;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Map;

class Transfer {

    @NotNull(message = "From account number must be set")
    private Integer fromAccountNumber;

    @NotNull(message = "To account number must be set")
    private Integer toAccountNumber;

    @Min(message = "Amount must be >= 0", value = 0)
    private int amount;

    Transfer(Integer fromAccountNumber, Integer toAccountNumber, int amount) {
        this.fromAccountNumber = fromAccountNumber;
        this.toAccountNumber = toAccountNumber;
        this.amount = amount;
    }

    @JsonCreator
    Transfer(Map<String, Integer> map) {
        this(map.get("fromAccountNumber"), map.get("toAccountNumber"), map.get("amount"));
    }

    Integer getFromAccountNumber() {
        return fromAccountNumber;
    }

    Integer getToAccountNumber() {
        return toAccountNumber;
    }

    int getAmount() {
        return amount;
    }
}
