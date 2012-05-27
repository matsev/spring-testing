package com.jayway.controller;

import com.jayway.service.AccountService;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Ignore
public class BankControllerTestConfiguration {

    @Autowired
    private AccountService accountService;

    @Bean
    BankController bankController() {
        return new BankController(accountService);
    }
}
