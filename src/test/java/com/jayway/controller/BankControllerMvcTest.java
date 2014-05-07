package com.jayway.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.service.AccountService;
import com.jayway.service.ImmutableAccount;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class BankControllerMvcTest {

    @Mock
    AccountService accountServiceMock;

    MockMvc mockMvc;


    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new BankController(accountServiceMock))
                .build();
    }


    @Test
    public void shouldPrint() throws Exception {
        ImmutableAccount account = new ImmutableAccount(1, 100);
        when(accountServiceMock.get(1)).thenReturn(account);

        mockMvc
                .perform(get("/accounts/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    public void shouldGetAccount() throws Exception {
        ImmutableAccount account = new ImmutableAccount(1, 100);
        when(accountServiceMock.get(1)).thenReturn(account);

        mockMvc
                .perform(get("/accounts/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("accountNumber").value(1))
                .andExpect(jsonPath("balance").value(100));
    }


    @Test
    public void shouldGetAllAccounts() throws Exception {
        when(accountServiceMock.getAllAccountNumbers()).thenReturn(Arrays.asList(1, 2));

        mockMvc
                .perform(get("/accounts")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("[0]").value(1))
                .andExpect(jsonPath("[1]").value(2));
    }


    @Test
    public void shouldDepositToAccount() throws Exception {
        Map<String, Integer> body = Collections.singletonMap("amount", 50);
        String json = toJsonString(body);

        mockMvc
                .perform(post("/accounts/1/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNoContent());

        verify(accountServiceMock).deposit(1, 50);
    }


    @Test
    public void shouldDeleteAccount() throws Exception {
        mockMvc
                .perform(delete("/accounts/1"))
                .andExpect(status().isNoContent());

        verify(accountServiceMock).deleteAccount(1);
    }


    @Test
    public void shouldNotDepositNegativeAmount() throws Exception {
        Map<String, Integer> body = Collections.singletonMap("amount", -50);
        String json = toJsonString(body);

        mockMvc
                .perform(post("/accounts/1/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        verifyZeroInteractions(accountServiceMock);
    }


    @Test
    public void shouldWithdrawFromAccount() throws Exception {
        Map<String, Integer> body = Collections.singletonMap("amount", 50);
        String json = toJsonString(body);

        mockMvc
                .perform(post("/accounts/1/withdraw")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(accountServiceMock).withdraw(1, 50);
    }


    @Test
    public void shouldNotWithdrawNegativeAmount() throws Exception {
        Map<String, Integer> body = Collections.singletonMap("amount", -50);
        String json = toJsonString(body);

        mockMvc
                .perform(post("/accounts/1/withdraw")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        verifyZeroInteractions(accountServiceMock);
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

