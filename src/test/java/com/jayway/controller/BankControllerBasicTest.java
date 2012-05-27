package com.jayway.controller;

import com.jayway.service.AccountService;
import com.jayway.service.ImmutableAccount;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BankControllerBasicTest {
    
    
    @Mock
    AccountService accountServiceMock;
    
    @Mock
    ImmutableAccount immutableAccountMock;
    
    @InjectMocks
    BankController bankController;


    @Test
    public void shouldGetAccount() {
        when(accountServiceMock.get(1L)).thenReturn(immutableAccountMock);

        ImmutableAccount account = bankController.get(1L);

        assertThat(account, is(immutableAccountMock));
    }


    @Test
    public void shouldDepositToAccount() {
        bankController.deposit(1L, new Amount(50L));

        verify(accountServiceMock).deposit(1L, 50L);
    }
    
    
    @Test
    public void shouldDeleteAccount() {
        bankController.delete(1L);
        
        verify(accountServiceMock).deleteAccount(1L);
    }
}
