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
public class AccountServiceImplMockitoTest {

    @Mock
    AccountRepository accountRepositoryMock;

    @Mock
    AccountEntity accountEntityMock;

    @InjectMocks
    AccountServiceImpl accountServiceImpl;


    @Test
    public void shouldDepositToAccount() {
        when(accountRepositoryMock.findOne(1)).thenReturn(accountEntityMock);

        accountServiceImpl.deposit(1, 100);

        verify(accountEntityMock).deposit(100);
    }


    @Test
    public void shouldWithdrawFromAccount() {
        AccountEntity returnedAccountEntity = mock(AccountEntity.class);
        when(accountRepositoryMock.findOne(2)).thenReturn(accountEntityMock);
        when(accountRepositoryMock.save(accountEntityMock)).thenReturn(returnedAccountEntity);

        accountServiceImpl.withdraw(2, 200);

        verify(accountEntityMock).withdraw(200);
    }


    @Test
    public void shouldTransferBetweenAccount() {
        AccountEntity fromAccountMock = mock(AccountEntity.class);
        AccountEntity toAccountMock = mock(AccountEntity.class);

        when(accountRepositoryMock.findOne(1)).thenReturn(fromAccountMock);
        when(accountRepositoryMock.findOne(2)).thenReturn(toAccountMock);

        accountServiceImpl.transfer(1, 2, 500);

        verify(fromAccountMock).withdraw(500);
        verify(toAccountMock).deposit(500);
    }

}
