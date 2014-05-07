package com.jayway.repository;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class AccountEntityTest {


    AccountEntity accountEntity;

    
    @Before
    public void setUp() {
        accountEntity = new AccountEntity();
    }


    @Test
    public void initialBalanceShouldBeZero() {
        assertThat(accountEntity.getBalance(), is(0));
    }
    
    
    @Test
    public void shouldDeposit() {
        accountEntity.deposit(10);
        assertThat(accountEntity.getBalance(), is(10));
    }


    @Test
    public void shouldWithdraw() {
        accountEntity.withdraw(10);
        assertThat(accountEntity.getBalance(), is(-10));
    }

}
