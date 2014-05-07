package com.jayway.controller;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

class Transfer {

    @NotNull(message = "From account number must be set")
    private Integer fromAccountNumber;

    @NotNull(message = "To account number must be set")
    private Integer toAccountNumber;

    @Min(message = "Amount must be >= 0", value = 0)
    private int amount;


    Integer getFromAccountNumber() {
        return fromAccountNumber;
    }


    void setFromAccountNumber(Integer fromAccountNumber) {
        this.fromAccountNumber = fromAccountNumber;
    }


    Integer getToAccountNumber() {
        return toAccountNumber;
    }


    void setToAccountNumber(Integer toAccountNumber) {
        this.toAccountNumber = toAccountNumber;
    }


    int getAmount() {
        return amount;
    }


    void setAmount(int amount) {
        this.amount = amount;
    }
}
