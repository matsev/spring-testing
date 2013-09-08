package com.jayway.controller;

import com.jayway.service.AccountService;
import com.jayway.service.ImmutableAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@Controller
class BankController {

    private final AccountService accountService;


    @Autowired
    BankController(AccountService accountService) {
        this.accountService = accountService;
    }


    @RequestMapping(value = "/accounts/{accountNumber}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ImmutableAccount get(@PathVariable("accountNumber") long accountId) {
        return accountService.get(accountId);
    }


    @RequestMapping(value = "/accounts/{accountNumber}/deposit",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)   // 204
    void deposit(@PathVariable("accountNumber") long accountNumber,
                 @Valid @RequestBody Amount amount)  {
        accountService.deposit(accountNumber, amount.getAmount());
    }


    @RequestMapping(value = "/accounts/{accountNumber}/withdraw",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ImmutableAccount withdraw(@PathVariable("accountNumber") long accountNumber,
                              @Valid @RequestBody Amount amount) {
        return accountService.withdraw(accountNumber, amount.getAmount());
    }


    @RequestMapping(value = "/accounts",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    List<Long> getAll() {
        return accountService.getAllAccountNumbers();
    }


    @RequestMapping(value = "/accounts",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)   // 201
    void create(HttpServletRequest request, HttpServletResponse response) {
        long accountNumber = accountService.createAccount();
        String locationHeader = createLocationHeader(request, accountNumber);
        response.addHeader("Location", locationHeader);
    }


    @RequestMapping(value = "/accounts/{accountNumber}",
            method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)   // 204
    void delete(@PathVariable("accountNumber") long accountNumber) {
        accountService.deleteAccount(accountNumber);
    }


    @RequestMapping(value = "/transfer",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)  // 204
    void transfer(@Valid @RequestBody Transfer transfer) {
        accountService.transfer(transfer.getFromAccountNumber(), transfer.getToAccountNumber(), transfer.getAmount());
    }


    @ExceptionHandler(TransactionSystemException.class)
    @ResponseStatus(HttpStatus.CONFLICT)   // 409
    void constraintViolation() {}


    private String createLocationHeader(HttpServletRequest request, long accountNumber) {
        StringBuffer url = request.getRequestURL();
        UriTemplate template = new UriTemplate(url.append("/{accountNumber}").toString());
        return template.expand(accountNumber).toASCIIString();
    }
}
