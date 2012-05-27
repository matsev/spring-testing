package com.jayway.service;

import java.util.List;

public interface AccountService {


    ImmutableAccount get(Long accountNumber) throws UnknownAccountException;


    void deposit(Long accountNumber, long amount) throws UnknownAccountException;


    ImmutableAccount withdraw(Long accountNumber, long amount) throws UnknownAccountException;


    void transfer(Long fromAccountNumber, Long toAccountNumber, long amount) throws UnknownAccountException;


    Long createAccount();


    void deleteAccount(Long accountNumber) throws UnknownAccountException;


    List<Long> getAllAccountNumbers();
}
