package com.jayway.controller;


import com.jayway.service.ImmutableAccount;
import com.jayway.service.UnknownAccountException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/application-context.xml", "/bank-servlet.xml"})
@TransactionConfiguration
@Transactional
@ActiveProfiles("h2")
public class BankControllerTransactionalTest {


    @Autowired
    BankController bankController;


    @Test
    public void shouldGetAccount() {
        ImmutableAccount immutableAccount = bankController.get(1L);

        assertThat(immutableAccount.getBalance(), is(100L));
    }


    @Test
    public void shouldDepositToAccount() {
        bankController.deposit(1L, new Amount(50L));

        ImmutableAccount immutableAccount = bankController.get(1L);

        assertThat(immutableAccount.getBalance(), is(150L));
    }


    @Test(expected = UnknownAccountException.class)
    public void shouldDeleteAccount() {
        bankController.delete(1L);

        bankController.get(1L);
    }
}
