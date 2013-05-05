package com.jayway.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.service.AccountService;
import com.jayway.service.ImmutableAccount;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

import java.util.Collections;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BankControllerRequestTest {

    MockHttpServletRequest requestMock;
    MockHttpServletResponse responseMock;
    AnnotationMethodHandlerAdapter handlerAdapter;
    ObjectMapper mapper;

    BankController bankController;

    @Mock
    AccountService accountServiceMock;

    @Before
    public void setUp() {
        bankController = new BankController(accountServiceMock);
        requestMock = new MockHttpServletRequest();
        requestMock.setContentType(MediaType.APPLICATION_JSON_VALUE);
        requestMock.addHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        responseMock = new MockHttpServletResponse();

        handlerAdapter = new AnnotationMethodHandlerAdapter();
        HttpMessageConverter[] messageConverters = {new MappingJackson2HttpMessageConverter()};
        handlerAdapter.setMessageConverters(messageConverters);

        mapper = new ObjectMapper();
    }


    @After
    public void tearDown() {
        reset(accountServiceMock);
    }


    @Test
    public void shouldGetAccount() throws Exception {
        ImmutableAccount account = new ImmutableAccount(1L, 100L);
        when(accountServiceMock.get(1L)).thenReturn(account);

        requestMock.setMethod("GET");
        requestMock.setRequestURI("/accounts/1");

        handlerAdapter.handle(requestMock, responseMock, bankController);

        assertThat(responseMock.getStatus(), is(HttpStatus.SC_OK));

        Map<String, String> responseBody = mapper.readValue(responseMock.getContentAsString(),
                new TypeReference<Map<String, String>>() {});

        assertThat(responseBody.get("accountNumber"), is("1"));
        assertThat(responseBody.get("balance"), is("100"));
    }


    @Test
    public void shouldDepositToAccount() throws Exception {
        requestMock.setMethod("POST");
        requestMock.setRequestURI("/accounts/1/deposit");
        Map<String, Long> body = Collections.singletonMap("amount", 50L);
        String json = toJsonString(body);
        requestMock.setContent(json.getBytes());

        handlerAdapter.handle(requestMock, responseMock, bankController);

        verify(accountServiceMock).deposit(1L, 50L);
    }


    @Test
    public void shouldDeleteAccount() throws Exception {
        requestMock.setMethod("DELETE");
        requestMock.setRequestURI("/accounts/1");

        handlerAdapter.handle(requestMock, responseMock, bankController);

        verify(accountServiceMock).deleteAccount(1L);
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
