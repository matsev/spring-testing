package com.jayway.service;

import com.jayway.domain.Account;

public class ImmutableAccount implements Account {

    private final Long accountNumber;

    private final long balance;


    public ImmutableAccount(Long accountNumber, long balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }


    @Override
    public Long getAccountNumber() {
        return accountNumber;
    }


    @Override
    public long getBalance() {
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
        result = 31 * result + (int) (balance ^ (balance >>> 32));
        return result;
    }
}
