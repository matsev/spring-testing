package com.jayway.service;

import org.junit.Ignore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Configuration
@Ignore
@Profile("accountServiceMock")
public class AccountServiceMockConfiguration {

    @Bean
    AccountService accountService() {
        AccountService accountServiceMock = mock(AccountService.class);
        when(accountServiceMock.get(1L)).thenReturn(new ImmutableAccount(1L, 100L));
        when(accountServiceMock.get(2L)).thenReturn(new ImmutableAccount(2L, 100L));
        return accountServiceMock;
    }

}
