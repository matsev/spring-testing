package com.jayway.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.jayway.restassured.RestAssured.baseURI;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/application-context.xml")
@ActiveProfiles("mysql")
public class BankApplicationIT {

    @Autowired
    DataSource dataSource;


    @Before
    public void setUp() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("TRUNCATE TABLE account_t");
        jdbcTemplate.execute("INSERT INTO account_t (account_number, balance) VALUES (1, 100)");
        jdbcTemplate.execute("INSERT INTO account_t (account_number, balance) VALUES (2, 200)");
    }


    @Test
    public void shouldLog() {
        given().
                auth().
                    basic("user", "secret").
                log().all().
        expect().
                log().all().
        when().
                get("/accounts/1");
    }


    @Test
    public void shouldGetSingleAccount() {
        given().
                auth().
                    basic("user", "secret").
        expect().
                response().
                    statusCode(HttpStatus.SC_OK).
                    contentType(MediaType.APPLICATION_JSON_VALUE).
                    body("accountNumber", is(1)).
                    body("balance", is(100)).
        when().
                get("/accounts/1");
    }


    @Test
    public void shouldDeleteAccount() {
        given().
                auth().
                basic("user", "secret").
        expect().
                response().
                    statusCode(HttpStatus.SC_NO_CONTENT).
        when().
                delete("/accounts/1");
    }


    @Test
    public void shouldDepositToAccount() {
        Map<String, Long> body = Collections.singletonMap("amount", 10L);
        String json = toJsonString(body);

        given().
                auth().
                    basic("user", "secret").
                request().
                    contentType(MediaType.APPLICATION_JSON_VALUE).
                    body(json).
        expect().
                response().
                    statusCode(HttpStatus.SC_NO_CONTENT).
        when().
                post("/accounts/1/deposit");
    }


    @Test
    public void shouldNotDepositNegativeAmount() {
        Map<String, Long> body = Collections.singletonMap("amount", -10L);
        String json = toJsonString(body);

        given().
                auth().
                    basic("user", "secret").
                request().
                    contentType(MediaType.APPLICATION_JSON_VALUE).
                    body(json).
        expect().
                response().
                    statusCode(HttpStatus.SC_BAD_REQUEST).
        when().
                post("/accounts/1/deposit");
    }


    @Test
    public void shouldWithdrawFromAccount() {
        Map<String, Long> body = Collections.singletonMap("amount", 10L);
        String json = toJsonString(body);

        given().
                auth().
                    basic("user", "secret").
                request().
                    contentType(MediaType.APPLICATION_JSON_VALUE).
                    body(json).
        expect().
                response().
                    statusCode(HttpStatus.SC_OK).
                    body("accountNumber", is(1)).
                    body("balance", is(greaterThan(0))).
        when().
                post("/accounts/1/withdraw");
    }


    @Test
    public void shouldNotOverdraw() {
        Map<String, Long> body = Collections.singletonMap("amount", 200L);
        String json = toJsonString(body);

        given().
                auth().
                    basic("user", "secret").
                request().
                    contentType(MediaType.APPLICATION_JSON_VALUE).
                    body(json).
        expect().
                response().
                    statusCode(HttpStatus.SC_CONFLICT).
        when().
                post("/accounts/1/withdraw");
    }


    @Test
    public void shouldGetAccounts() {
        given().
                auth().
                    basic("user", "secret").
        expect().
                response().
                    statusCode(HttpStatus.SC_OK).
                    contentType(MediaType.APPLICATION_JSON_VALUE).
                    body("size()", is(2)).
                    body("findAll {it}", hasItems(1, 2)).
        when().
                get("/accounts");
    }


    @Test
    public void shouldCreateAccount() {
        given().
                auth().
                    basic("user", "secret").
        expect().
                response().
                    statusCode(HttpStatus.SC_CREATED).
                    header(HttpHeaders.LOCATION, startsWith(baseURI)).
                    header(HttpHeaders.LOCATION, containsString("/accounts/")).
        when().
                post("/accounts");
    }


    @Test
    public void shouldNotGetUnknownAccount() {
        given().
                auth().
                    basic("user", "secret").
        expect().
                response().
                    statusCode(HttpStatus.SC_NOT_FOUND).
        when().
                get("/accounts/0");
    }


    @Test
    public void shouldNotOverdrawDuringTransfer() {
        Map<String, Long> body = new HashMap<String, Long>(){{
            put("fromAccountNumber", 1L);
            put("toAccountNumber", 2L);
            put("amount", 300L);
        }};
        String json = toJsonString(body);

        given().
                auth().
                    basic("user", "secret").
                request().
                    contentType(MediaType.APPLICATION_JSON_VALUE).
                    body(json).
        expect().
                response().
                    statusCode(HttpStatus.SC_CONFLICT).
        when().
                post("/transfer");
    }


    private String toJsonString(Map<String, ?> map) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
