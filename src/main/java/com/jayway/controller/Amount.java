package com.jayway.controller;

import javax.validation.constraints.Min;

class Amount {

    @Min(message = "Amount must be >= 0", value = 0)
    private long amount;

    Amount() {
    }

    Amount(long amount) {
        this.amount = amount;
    }

    long getAmount() {
        return amount;
    }


    void setAmount(long amount) {
        this.amount = amount;
    }
}
