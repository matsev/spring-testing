package com.jayway.service;

import com.jayway.domain.Account;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:application-context.xml")
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
@ActiveProfiles("test")
public class AccountServiceImplTest {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    AccountService accountService;

    @Autowired
    DataSource dataSource;

    JdbcTemplate jdbcTemplate;


    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Test
    public void shouldGetAccount() {
        Account account = accountService.get(1L);

        assertThat(account.getAccountNumber(), is(1L));
        assertThat(account.getBalance(), is(100L));
    }


    @Test(expected = UnknownAccountException.class)
    public void shouldThrowExceptionForFindingUnknownAccountNumber() {
        accountService.get(-1L);
    }


    @Test
    public void shouldDeposit() {
        accountService.deposit(1L, 100);

        entityManager.flush();

        long balance = getBalance(1L);
        assertThat(balance, is(200L));
    }


    @Test
    public void shouldWithdraw() {
        Account account = accountService.withdraw(1L, 50);

        entityManager.flush();

        assertThat(account.getBalance(), is(50L));
        
        long balance = getBalance(1L);
        assertThat(balance, is(50L));
    }


    @Test
    public void shouldNotOverdraw() {
        try {
            accountService.withdraw(1L, 200);
            entityManager.flush();
            fail("Expected ConstraintViolationException");
        } catch (ConstraintViolationException e) {
            // Expected
        }

        long balance = getBalance(1L);
        assertThat(balance, is(100L));
    }


    @Test
    public void shouldTransfer() {
        accountService.transfer(1L, 2L, 10);
        entityManager.flush();

        long firstBalance = getBalance(1L);
        long secondBalance = getBalance(2L);

        assertThat(firstBalance, is(90L));
        assertThat(secondBalance, is(210L));
    }


    @Test
    public void shouldNotTransferIfOverdraw() {
        try {
            accountService.transfer(1L, 2L, 200);
            entityManager.flush();
            fail("Expected ConstraintViolationException");
        } catch (ConstraintViolationException e) {
            // Expected
        }

        long firstBalance = getBalance(1L);
        long secondBalance = getBalance(2L);

        assertThat(firstBalance, is(100L));
        assertThat(secondBalance, is(200L));
    }


    @Test
    public void shouldNotTransferFromUnknownAccount() {
        try {
            accountService.transfer(-1L, 2L, 50);
            entityManager.flush();
            fail("Expected UnknownAccountException");
        } catch (UnknownAccountException e) {
            // Expected
        }

        long secondBalance = getBalance(2L);

        assertThat(secondBalance, is(200L));
    }


    @Test
    public void shouldNotTransferToUnknownAccount() {
        try {
            accountService.transfer(1L, -2L, 50);
            entityManager.flush();
            fail("Expected UnknownAccountException");
        } catch (UnknownAccountException e) {
            // Expected
        }

        long secondBalance = getBalance(1L);

        assertThat(secondBalance, is(100L));
    }


    @Test
    public void shouldGetAllAccountNumbers() {
        List<Long> allAccounts = accountService.getAllAccountNumbers();

        assertThat(allAccounts, hasItems(1L, 2L));
    }


    @Test(expected = UnknownAccountException.class)
    public void shouldThrowExceptionWhenDeletingUnknownAccountNumber() {
        accountService.deleteAccount(-1L);
    }


    long getBalance(Long accountNumber) {
        return jdbcTemplate.queryForLong("SELECT balance FROM account_t WHERE account_number = ?", accountNumber);
    }
}
