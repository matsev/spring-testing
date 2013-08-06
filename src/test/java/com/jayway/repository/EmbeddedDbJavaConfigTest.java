package com.jayway.repository;


import com.jayway.config.InMemoryRepositoryConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = InMemoryRepositoryConfig.class)
@ActiveProfiles("test")
public class EmbeddedDbJavaConfigTest {


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
        return jdbcTemplate.queryForObject("SELECT balance FROM account_t WHERE account_number = ?", Long.class, accountNumber);
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
