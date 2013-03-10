package com.jayway.service;

import com.jayway.repository.AccountEntity;
import com.jayway.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    
    @Autowired
    AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    @Transactional(readOnly = true)
    @Override
    public ImmutableAccount get(Long accountNumber) {
        AccountEntity account = getAccountEntity(accountNumber);
        return new ImmutableAccount(account.getAccountNumber(), account.getBalance());
    }


    @Override
    public void deposit(Long accountNumber, long amount) {
        AccountEntity account = getAccountEntity(accountNumber);
        account.deposit(amount);
        accountRepository.save(account);
    }


    @Override
    public ImmutableAccount withdraw(Long accountNumber, long amount) {
        AccountEntity account = getAccountEntity(accountNumber);
        account.withdraw(amount);
        AccountEntity savedAccount = accountRepository.save(account);
        return new ImmutableAccount(savedAccount.getAccountNumber(), savedAccount.getBalance());
    }


    @Override
    public void transfer(Long fromAccountNumber, Long toAccountNumber, long amount) {
        AccountEntity fromAccount = getAccountEntity(fromAccountNumber);
        AccountEntity toAccount = getAccountEntity(toAccountNumber);
        fromAccount.withdraw(amount);
        toAccount.deposit(amount);
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }


    @Override
    public Long createAccount() {
        AccountEntity account = new AccountEntity();
        AccountEntity savedAccount = accountRepository.save(account);
        return savedAccount.getAccountNumber();
    }


    @Override
    public void deleteAccount(Long accountNumber) throws UnknownAccountException {
        try {
            accountRepository.delete(accountNumber);
        } catch (EmptyResultDataAccessException e) {
            throw new UnknownAccountException(accountNumber, e);
        }
    }


    @Transactional(readOnly = true)
    @Override
    public List<Long> getAllAccountNumbers() {
        List<AccountEntity> accounts = accountRepository.findAll();
        List<Long> result = new ArrayList<Long>(accounts.size());
        for (AccountEntity account : accounts) {
            result.add(account.getAccountNumber());
        }
        return result;
    }


    private AccountEntity getAccountEntity(Long accountNumber) throws UnknownAccountException {
        AccountEntity account = accountRepository.findOne(accountNumber);
        if (account == null) {
            throw new UnknownAccountException(accountNumber);
        }
        return account;
    }
}
