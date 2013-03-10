package com.jayway.repository;

import com.jayway.domain.Account;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Entity
@Table(name = "account_t")
public class AccountEntity implements Account {

    @Id
    @Column(name = "account_number")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountNumber;

    @Column(name = "balance")
    @Min(message = "Balance must be >= 0", value = 0)
    private long balance;


    @Override
    public Long getAccountNumber() {
        return accountNumber;
    }


    @Override
    public long getBalance() {
        return balance;
    }


    public void deposit(long amount) {
        balance += amount;
    }


    public void withdraw(long amount) {
        balance -= amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountEntity)) return false;

        AccountEntity that = (AccountEntity) o;

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
