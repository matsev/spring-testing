package com.jayway.application;

import com.jayway.restassured.http.ContentType;
import net.minidev.json.JSONObject;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class BankApplicationIT {


    @Test
    public void shouldGetSingleAccount() {
        expect().
                statusCode(HttpStatus.SC_OK).
                contentType(ContentType.JSON).
                body("accountNumber", is(1)).
                body("balance", is(100)).
        when().
                get("/accounts/1");
    }


    @Test
    public void shouldDepositToAccount() {
        Map<String, Long> body = Collections.singletonMap("amount", 10L);
        JSONObject jsonBody = new JSONObject(body);

        given().
                request().
                    contentType(MediaType.APPLICATION_JSON_VALUE).
                    body(jsonBody).
        expect().
                response().
                    statusCode(HttpStatus.SC_NO_CONTENT).
        when().
                post("/accounts/1/deposit");
    }


    @Test
    public void shouldNotDepositNegativeAmount() {
        Map<String, Long> body = Collections.singletonMap("amount", -10L);
        JSONObject jsonBody = new JSONObject(body);

        given().
                request().
                    contentType(MediaType.APPLICATION_JSON_VALUE).
                    body(jsonBody).
        expect().
                response().
                    statusCode(HttpStatus.SC_BAD_REQUEST).
        when().
                post("/accounts/1/deposit");
    }


    @Test
    public void shouldWithdrawFromAccount() {
        Map<String, Long> body = Collections.singletonMap("amount", 10L);
        JSONObject jsonBody = new JSONObject(body);

        given().
                request().
                    contentType(MediaType.APPLICATION_JSON_VALUE).
                    body(jsonBody).
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
        JSONObject jsonBody = new JSONObject(body);

        given().
                contentType(ContentType.JSON).
                body(jsonBody).
        expect().
                statusCode(HttpStatus.SC_CONFLICT).
        when().
                post("/accounts/1/withdraw");
    }


    @Test
    @Ignore
    public void shouldGetAccounts() {
        expect().
                statusCode(HttpStatus.SC_OK).
                contentType(ContentType.JSON).
                body("size()", is(2)).
                body("findAll {it}", hasItems(1, 2)).
        when().
                get("/accounts");
    }


    @Test
    public void shouldCreateAccount() {
        expect().
                statusCode(HttpStatus.SC_CREATED).
                header(HttpHeaders.LOCATION, startsWith(baseURI)).
                header(HttpHeaders.LOCATION, containsString("/accounts/")).
        when().
                post("/accounts");
    }


    @Test
    public void shouldNotGetUnknownAccount() {
        expect().
                statusCode(HttpStatus.SC_NOT_FOUND).
        when().
                get("/accounts/0");
    }

    @Test
    public void shouldNotOverdrawDuringWithdraw() {
        Map<String, Long> body = new HashMap<String, Long>(){{
            put("fromAccountNumber", 1L);
            put("toAccountNumber", 2L);
            put("amount", 300L);
        }};
        JSONObject jsonBody = new JSONObject(body);

        given().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                body(jsonBody).
        expect().
                statusCode(HttpStatus.SC_CONFLICT).
        when().
                post("/transfer");
    }
}
