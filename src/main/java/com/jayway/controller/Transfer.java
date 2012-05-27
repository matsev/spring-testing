package com.jayway.controller;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

class Transfer {

    @NotNull(message = "From account number must be set")
    private Long fromAccountNumber;

    @NotNull(message = "To account number must be set")
    private Long toAccountNumber;

    @Min(message = "Amount must be >= 0", value = 0)
    private long amount;


    Long getFromAccountNumber() {
        return fromAccountNumber;
    }


    void setFromAccountNumber(Long fromAccountNumber) {
        this.fromAccountNumber = fromAccountNumber;
    }


    Long getToAccountNumber() {
        return toAccountNumber;
    }


    void setToAccountNumber(Long toAccountNumber) {
        this.toAccountNumber = toAccountNumber;
    }


    long getAmount() {
        return amount;
    }


    void setAmount(long amount) {
        this.amount = amount;
    }
}
