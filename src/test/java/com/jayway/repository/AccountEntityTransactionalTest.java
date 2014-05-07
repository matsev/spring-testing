package com.jayway.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = EmbeddedDbJavaConfig.class)
// @ContextConfiguration("/embedded-db-application-context.xml")
@Transactional
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class AccountEntityTransactionalTest {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    AccountRepository accountRepository;


    @Test
    public void canFindAccount() {
        AccountEntity account = accountRepository.findOne(1);

        assertThat(account.getAccountNumber(), is(1));
        assertThat(account.getBalance(), is(100));
    }


    @Test
    public void shouldDeposit() {
        AccountEntity account = accountRepository.findOne(1);
        account.deposit(50);

        entityManager.flush();

        AccountEntity updated = accountRepository.findOne(1);
        assertThat(updated.getBalance(), is(150));
    }


    @Test
    public void verifyBalance() {
        AccountEntity account = accountRepository.findOne(1);

        assertThat(account.getAccountNumber(), is(1));
        assertThat(account.getBalance(), is(100));
    }


    @Test
    public void shouldWithdraw() {
        AccountEntity account = accountRepository.findOne(1);
        account.withdraw(20);

        entityManager.flush();

        AccountEntity updated = accountRepository.findOne(1);
        assertThat(updated.getBalance(), is(80));
    }


    @Test(expected = ConstraintViolationException.class)
    public void shouldNotAcceptNegativeBalance() {
        AccountEntity account = accountRepository.findOne(1);
        account.withdraw(200);

        entityManager.flush();

        AccountEntity updated = accountRepository.findOne(1);
        assertThat(updated.getBalance(), is(-100));
    }
}
