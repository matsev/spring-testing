package com.jayway.service;

import com.jayway.config.ApplicationConfig;
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
@ContextConfiguration(classes = ApplicationConfig.class)
// @ContextConfiguration("classpath:application-context.xml")
@ActiveProfiles("h2")
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
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
        Account account = accountService.get(1);

        assertThat(account.getAccountNumber(), is(1));
        assertThat(account.getBalance(), is(100));
    }


    @Test(expected = UnknownAccountException.class)
    public void shouldThrowExceptionForFindingUnknownAccountNumber() {
        accountService.get(-1);
    }


    @Test
    public void shouldDeposit() {
        accountService.deposit(1, 100);

        entityManager.flush();

        int balance = getBalance(1);
        assertThat(balance, is(200));
    }


    @Test
    public void shouldWithdraw() {
        accountService.withdraw(1, 50);

        entityManager.flush();

        int balance = getBalance(1);
        assertThat(balance, is(50));
    }


    @Test
    public void shouldNotOverdraw() {
        try {
            accountService.withdraw(1, 200);
            entityManager.flush();
            fail("Expected ConstraintViolationException");
        } catch (ConstraintViolationException e) {
            // Expected
        }

        int balance = getBalance(1);
        assertThat(balance, is(100));
    }


    @Test
    public void shouldTransfer() {
        accountService.transfer(1, 2, 10);
        entityManager.flush();

        int firstBalance = getBalance(1);
        assertThat(firstBalance, is(90));

        int secondBalance = getBalance(2);
        assertThat(secondBalance, is(210));
    }


    @Test
    public void shouldNotTransferIfOverdraw() {
        try {
            accountService.transfer(1, 2, 200);
            entityManager.flush();
            fail("Expected ConstraintViolationException");
        } catch (ConstraintViolationException e) {
            // Expected
        }

        int firstBalance = getBalance(1);
        assertThat(firstBalance, is(100));

        int secondBalance = getBalance(2);
        assertThat(secondBalance, is(200));
    }


    @Test
    public void shouldNotTransferFromUnknownAccount() {
        try {
            accountService.transfer(-1, 2, 50);
            entityManager.flush();
            fail("Expected UnknownAccountException");
        } catch (UnknownAccountException e) {
            // Expected
        }

        int secondBalance = getBalance(2);
        assertThat(secondBalance, is(200));
    }


    @Test
    public void shouldNotTransferToUnknownAccount() {
        try {
            accountService.transfer(1, -2, 50);
            entityManager.flush();
            fail("Expected UnknownAccountException");
        } catch (UnknownAccountException e) {
            // Expected
        }

        int balance = getBalance(1);
        assertThat(balance, is(100));
    }


    @Test
    public void shouldGetAllAccountNumbers() {
        List<Integer> allAccounts = accountService.getAllAccountNumbers();

        assertThat(allAccounts, hasItems(1, 2));
    }


    @Test(expected = UnknownAccountException.class)
    public void shouldThrowExceptionWhenDeletingUnknownAccountNumber() {
        accountService.deleteAccount(-1);
    }


    int getBalance(Integer accountNumber) {
        return jdbcTemplate.queryForObject("SELECT balance FROM account_t WHERE account_number = ?", Integer.class, accountNumber);
    }
}
