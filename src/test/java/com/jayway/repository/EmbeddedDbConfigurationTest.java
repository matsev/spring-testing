package com.jayway.repository;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/embedded-db-application-context.xml")
public class EmbeddedDbConfigurationTest {


    JdbcTemplate jdbcTemplate;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    DataSource dataSource;


    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


    long getBalance(long accountNumber) {
        return jdbcTemplate.queryForLong("SELECT balance FROM account_t WHERE account_number = ?", accountNumber);
    }


    @Test
    public void verifyEmbeddedDatabase() {
        long firstBalance = getBalance(1);
        long secondBalance = getBalance(2);

        assertThat(firstBalance, is(100L));
        assertThat(secondBalance, is(200L));
    }


    @Test
    public void ormMappingShouldWork() {
        AccountEntity accountEntity = accountRepository.findOne(1L);

        assertThat(accountEntity.getAccountNumber(), is(1L));
        assertThat(accountEntity.getBalance(), is(100L));
    }
}
