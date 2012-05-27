package com.jayway.controller;


import com.jayway.service.AccountService;
import com.jayway.service.AccountServiceMockConfiguration;
import net.minidev.json.JSONObject;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

import java.util.Collections;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BankControllerTestConfiguration.class, AccountServiceMockConfiguration.class})
@ActiveProfiles("accountServiceMock")
public class BankControllerRequestTest {

    MockHttpServletRequest requestMock;
    MockHttpServletResponse responseMock;
    AnnotationMethodHandlerAdapter handlerAdapter;
    ObjectMapper mapper;

    @Autowired
    BankController bankController;

    @Autowired
    AccountService accountServiceMock;


    @Before
    public void setUp() {
        requestMock = new MockHttpServletRequest();
        requestMock.setContentType(MediaType.APPLICATION_JSON_VALUE);
        requestMock.addHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        responseMock = new MockHttpServletResponse();

        handlerAdapter = new AnnotationMethodHandlerAdapter();
        HttpMessageConverter[] messageConverters = {new MappingJacksonHttpMessageConverter()};
        handlerAdapter.setMessageConverters(messageConverters);

        mapper = new ObjectMapper();
    }


    @After
    public void tearDown() {
        reset(accountServiceMock);
    }


    @Test
    public void shouldGetAccount() throws Exception {
        requestMock.setMethod("GET");
        requestMock.setRequestURI("/account/1");

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
        requestMock.setRequestURI("/account/1/deposit");
        Map<String, Long> body = Collections.singletonMap("amount", 50L);
        JSONObject jsonBody = new JSONObject(body);
        requestMock.setContent(jsonBody.toString().getBytes());

        handlerAdapter.handle(requestMock, responseMock, bankController);

        verify(accountServiceMock).deposit(1L, 50L);
    }


    @Test
    public void shouldDeleteAccount() throws Exception {
        requestMock.setMethod("DELETE");
        requestMock.setRequestURI("/account/1");

        handlerAdapter.handle(requestMock, responseMock, bankController);

        verify(accountServiceMock).deleteAccount(1L);
    }

}
