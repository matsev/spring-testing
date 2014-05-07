package com.jayway.service;

import java.util.List;

public interface AccountService {


    ImmutableAccount get(Integer accountNumber) throws UnknownAccountException;


    void deposit(Integer accountNumber, int amount) throws UnknownAccountException;


    ImmutableAccount withdraw(Integer accountNumber, int amount) throws UnknownAccountException;


    void transfer(Integer fromAccountNumber, Integer toAccountNumber, int amount) throws UnknownAccountException;


    Integer createAccount();


    void deleteAccount(Integer accountNumber) throws UnknownAccountException;


    List<Integer> getAllAccountNumbers();
}
