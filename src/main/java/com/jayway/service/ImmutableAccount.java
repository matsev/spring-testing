package com.jayway.service;

import com.jayway.domain.Account;

public class ImmutableAccount implements Account {

    private final Integer accountNumber;

    private final int balance;


    public ImmutableAccount(Integer accountNumber, int balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }


    @Override
    public Integer getAccountNumber() {
        return accountNumber;
    }


    @Override
    public int getBalance() {
        return balance;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImmutableAccount)) return false;

        ImmutableAccount that = (ImmutableAccount) o;

        if (balance != that.balance) return false;
        if (accountNumber != null ? !accountNumber.equals(that.accountNumber) : that.accountNumber != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = accountNumber != null ? accountNumber.hashCode() : 0;
        result = 31 * result + balance;
        return result;
    }

    @Override
    public String toString() {
        return "ImmutableAccount{" +
                "accountNumber=" + accountNumber +
                ", balance=" + balance +
                '}';
    }
}
