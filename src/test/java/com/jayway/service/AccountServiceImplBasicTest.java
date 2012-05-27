package com.jayway.service;

import com.jayway.repository.AccountEntity;
import com.jayway.repository.AccountRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceImplBasicTest {

    @Mock
    AccountRepository accountRepositoryMock;

    @Mock
    AccountEntity accountEntityMock;

    @InjectMocks
    AccountServiceImpl accountServiceImpl;


    @Test
    public void shouldDepositToAccount() {
        when(accountRepositoryMock.findOne(1L)).thenReturn(accountEntityMock);

        accountServiceImpl.deposit(1L, 100L);

        verify(accountEntityMock).deposit(100L);
    }


    @Test
    public void shouldWithdrawFromAccount() {
        AccountEntity returnedAccountEntity = mock(AccountEntity.class);
        when(accountRepositoryMock.findOne(2L)).thenReturn(accountEntityMock);
        when(accountRepositoryMock.save(accountEntityMock)).thenReturn(returnedAccountEntity);

        accountServiceImpl.withdraw(2L, 200L);

        verify(accountEntityMock).withdraw(200L);
    }


    @Test
    public void shouldTransferBetweenAccount() {
        AccountEntity fromAccountMock = mock(AccountEntity.class);
        AccountEntity toAccountMock = mock(AccountEntity.class);

        when(accountRepositoryMock.findOne(1L)).thenReturn(fromAccountMock);
        when(accountRepositoryMock.findOne(2L)).thenReturn(toAccountMock);

        accountServiceImpl.transfer(1L, 2L, 500L);

        verify(fromAccountMock).withdraw(500L);
        verify(toAccountMock).deposit(500L);
    }

}
