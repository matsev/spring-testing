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
        assertThat(accountEntity.getBalance(), is(0L));
    }
    
    
    @Test
    public void shouldDeposit() {
        accountEntity.deposit(10L);
        assertThat(accountEntity.getBalance(), is(10L));
    }


    @Test
    public void shouldWithdraw() {
        accountEntity.withdraw(10L);
        assertThat(accountEntity.getBalance(), is(-10L));
    }

}
