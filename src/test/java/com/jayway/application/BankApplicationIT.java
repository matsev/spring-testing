package com.jayway.application;

import com.jayway.restassured.http.ContentType;
import net.minidev.json.JSONObject;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
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
                get("/account/1");
    }


    @Test
    public void shouldDepositToAccount() {
        Map<String, Long> body = Collections.singletonMap("amount", 50L);
        JSONObject jsonBody = new JSONObject(body);

        given().
                request().
                    contentType(MediaType.APPLICATION_JSON_VALUE).
                    body(jsonBody).
        expect().
                response().
                    statusCode(HttpStatus.SC_NO_CONTENT).
        when().
                post("/account/1/deposit");
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
                post("/account/1/deposit");
    }


    @Test
    public void shouldWithdrawFromAccount() {
        Map<String, Long> body = Collections.singletonMap("amount", 20L);
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
                post("/account/1/withdraw");
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
                post("/account/1/withdraw");
    }


    @Test
    public void shouldCreateAccount() {
        expect().
                statusCode(HttpStatus.SC_CREATED).
                header(HttpHeaders.LOCATION, startsWith(baseURI)).
                header(HttpHeaders.LOCATION, containsString("/account/")).
        when().
                post("/account");
    }


    @Test
    public void shouldNotGetUnknownAccount() {
        expect().
                statusCode(HttpStatus.SC_NOT_FOUND).
        when().
                get("/account/0");
    }

    @Test
    public void shouldNotOverdrawAccountDuringWithdraw() {
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
