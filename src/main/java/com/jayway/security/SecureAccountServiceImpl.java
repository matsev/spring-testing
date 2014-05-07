package com.jayway.security;

import com.jayway.service.AccountService;
import com.jayway.service.ImmutableAccount;
import com.jayway.service.UnknownAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Primary
@Service
class SecureAccountServiceImpl implements AccountService {

    private final AccountService accountService;


    @Autowired
    SecureAccountServiceImpl(AccountService accountService) {
        this.accountService = accountService;
    }


    @PreAuthorize("hasAuthority('ACCOUNT_OWNER')")
    @Override
    public ImmutableAccount get(Integer accountNumber) throws UnknownAccountException {
        return accountService.get(accountNumber);
    }


    @PreAuthorize("hasAuthority('ACCOUNT_OWNER')")
    @Override
    public void deposit(Integer accountNumber, int amount) throws UnknownAccountException {
        accountService.deposit(accountNumber, amount);
    }


    @PreAuthorize("hasAuthority('ACCOUNT_OWNER')")
    @Override
    public ImmutableAccount withdraw(Integer accountNumber, int amount) throws UnknownAccountException {
        return accountService.withdraw(accountNumber, amount);
    }


    @PreAuthorize("hasAuthority('ACCOUNT_OWNER')")
    @Override
    public void transfer(Integer fromAccountNumber, Integer toAccountNumber, int amount) throws UnknownAccountException {
        accountService.transfer(fromAccountNumber, toAccountNumber, amount);
    }


    @PreAuthorize("hasAuthority('ACCOUNT_OWNER')")
    @Override
    public Integer createAccount() {
        return accountService.createAccount();
    }


    @PreAuthorize("hasAuthority('ACCOUNT_OWNER')")
    @Override
    public void deleteAccount(Integer accountNumber) throws UnknownAccountException {
        accountService.deleteAccount(accountNumber);
    }


    @PreAuthorize("hasAuthority('ACCOUNT_OWNER')")
    @Override
    public List<Integer> getAllAccountNumbers() {
        return accountService.getAllAccountNumbers();
    }
}
