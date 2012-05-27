package com.jayway.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/embedded-db-application-context.xml")
@Transactional
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class AccountEntityTransactionalTest {

    @PersistenceContext
    EntityManager entityManager;
    
    @Autowired
    AccountRepository accountRepository;
    
    @Autowired
    DataSource dataSource;

    JdbcTemplate jdbcTemplate;


    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


    long getBalance(long accountNumber) {
        return jdbcTemplate.queryForLong("SELECT balance FROM account_t WHERE account_number = ?", accountNumber);
    }


    @Test
    public void canFindAccount() {
        AccountEntity account = accountRepository.findOne(1L);

        assertThat(account.getAccountNumber(), is(1L));
        assertThat(account.getBalance(), is(100L));
    }
    
    
    @Test
    public void shouldDeposit() {
        AccountEntity account = accountRepository.findOne(1L);
        account.deposit(50L);

        entityManager.flush();
        
        assertThat(getBalance(1L), is(150L));
    }


    @Test
    public void verifyBalance() {
        AccountEntity account = accountRepository.findOne(1L);

        assertThat(account.getAccountNumber(), is(1L));
        assertThat(account.getBalance(), is(100L));
    }


    @Test
    public void shouldWithdraw() {
        AccountEntity account = accountRepository.findOne(1L);
        account.withdraw(20L);

        entityManager.flush();

        assertThat(getBalance(1L), is(80L));
    }

}
