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
    private Integer accountNumber;

    @Column(name = "balance")
    @Min(message = "Balance must be >= 0", value = 0)
    private int balance;


    @Override
    public Integer getAccountNumber() {
        return accountNumber;
    }


    @Override
    public int getBalance() {
        return balance;
    }


    public void deposit(int amount) {
        balance += amount;
    }


    public void withdraw(int amount) {
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
        result = 31 * result + balance;
        return result;
    }

    @Override
    public String toString() {
        return "AccountEntity{" +
                "accountNumber=" + accountNumber +
                ", balance=" + balance +
                '}';
    }
}
