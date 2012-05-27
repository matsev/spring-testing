package com.jayway.controller;

import com.jayway.service.AccountService;
import com.jayway.service.AccountServiceMockConfiguration;
import net.minidev.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.server.MockMvc;
import org.springframework.test.web.server.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AccountServiceMockConfiguration.class)
@ActiveProfiles("accountServiceMock")
public class BankControllerMvcTest {


    @Autowired
    AccountService accountServiceMock;

    MockMvc mockMvc;


    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new BankController(accountServiceMock))
                .build();
    }


    @After
    public void tearDown() throws Exception {
        reset(accountServiceMock);
    }


    @Test
    public void shouldGetAccount() throws Exception {
        mockMvc
                .perform(get("/account/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().type(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("accountNumber").value(1))
                .andExpect(jsonPath("balance").value(100));
    }


    @Test
    public void shouldDepositToAccount() throws Exception {
        Map<String, Long> body = Collections.singletonMap("amount", 50L);
        JSONObject jsonBody = new JSONObject(body);

        mockMvc
                .perform(post("/account/1/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(jsonBody.toString().getBytes()))
                .andExpect(status().isNoContent());

        verify(accountServiceMock).deposit(1L, 50L);
    }


    @Test
    public void shouldDeleteAccount() throws Exception {
        mockMvc
                .perform(delete("/account/1"))
                .andExpect(status().isNoContent());

        verify(accountServiceMock).deleteAccount(1L);
    }


    @Test
    public void shouldNotDepositNegativeAmount() throws Exception {
        Map<String, Long> body = Collections.singletonMap("amount", -50L);
        JSONObject jsonBody = new JSONObject(body);

        mockMvc
                .perform(post("/account/1/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(jsonBody.toString().getBytes()))
                .andExpect(status().isBadRequest());

        verifyZeroInteractions(accountServiceMock);
    }


    @Test
    public void shouldWithdrawFromAccount() throws Exception {
        Map<String, Long> body = Collections.singletonMap("amount", 50L);
        JSONObject jsonBody = new JSONObject(body);

        mockMvc
                .perform(post("/account/1/withdraw")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(jsonBody.toString().getBytes()))
                .andExpect(status().isOk());

        verify(accountServiceMock).withdraw(1L, 50L);
    }


    @Test
    public void shouldNotWithdrawNegativeAmount() throws Exception {
        Map<String, Long> body = Collections.singletonMap("amount", -50L);
        JSONObject jsonBody = new JSONObject(body);

        mockMvc
                .perform(post("/account/1/withdraw")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(jsonBody.toString().getBytes()))
                .andExpect(status().isBadRequest());

        verifyZeroInteractions(accountServiceMock);
    }

}

