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
public class EmbeddedDbXmlConfigTest {


    JdbcTemplate jdbcTemplate;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    DataSource dataSource;


    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


    int getBalance(Integer accountNumber) {
        return jdbcTemplate.queryForObject(
                "SELECT balance FROM account_t WHERE account_number = ?",
                Integer.class, accountNumber);
    }


    @Test
    public void verifyEmbeddedDatabase() {
        int firstBalance = getBalance(1);
        assertThat(firstBalance, is(100));

        int secondBalance = getBalance(2);
        assertThat(secondBalance, is(200));
    }


    @Test
    public void ormMappingShouldWork() {
        AccountEntity accountEntity = accountRepository.findOne(1);

        assertThat(accountEntity.getAccountNumber(), is(1));
        assertThat(accountEntity.getBalance(), is(100));
    }
}
