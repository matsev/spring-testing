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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = EmbeddedDbJavaConfig.class)
// @ContextConfiguration("/embedded-db-application-context.xml")
@Transactional
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class AccountRepositoryTest {

    @PersistenceContext
    EntityManager entityManager;


    @Autowired
    AccountRepository accountRepository;


    @Test
    public void shouldGetAccountByNumber() {
        AccountEntity account = accountRepository.findOne(1);

        assertThat(account.getBalance(), is(100));
    }


    @Test
    public void newAccountsShouldHaveZeroBalance() {
        AccountEntity account = accountRepository.save(new AccountEntity());

        entityManager.flush();

        assertThat(account.getBalance(), is(0));
    }


    @Test
    public void canDeleteAccount()  {
        accountRepository.delete(1);

        entityManager.flush();

        assertThat(accountRepository.findOne(1), nullValue());
    }

}
