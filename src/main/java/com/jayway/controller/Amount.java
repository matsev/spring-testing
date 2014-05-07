package com.jayway.controller;

import javax.validation.constraints.Min;

class Amount {

    @Min(message = "Amount must be >= 0", value = 0)
    private int amount;

    Amount() {
    }

    Amount(int amount) {
        this.amount = amount;
    }

    int getAmount() {
        return amount;
    }


    void setAmount(int amount) {
        this.amount = amount;
    }
}
