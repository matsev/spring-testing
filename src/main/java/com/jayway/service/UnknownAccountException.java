package com.jayway.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UnknownAccountException extends RuntimeException {

    private static final long serialVersionUID = -5562010974917511663L;

    public UnknownAccountException(long accountNumber) {
        super("Unknown account number: " + accountNumber);
    }

    public UnknownAccountException(long accountNumber, Exception cause) {
        super("Unknown account number: " + accountNumber, cause);
    }
}
