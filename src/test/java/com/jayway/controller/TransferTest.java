package com.jayway.controller;


import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TransferTest {

    static Validator validator;
    Transfer transfer;


    @BeforeClass
    public static void beforeClass() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Before
    public void setUp() {
        transfer = new Transfer();
    }
    

    @Test
    public void shouldAllowZeroAmount() {
        transfer.setAmount(0);
        transfer.setFromAccountNumber(0);
        transfer.setToAccountNumber(0);

        Set<ConstraintViolation<Transfer>> constraintViolations =
                validator.validate(transfer);

        assertThat(constraintViolations.size(), is(0));
    }


    @Test
    public void shouldNotAllowNegativeAmount() {
        transfer.setAmount(-1);
        transfer.setFromAccountNumber(0);
        transfer.setToAccountNumber(0);

        Set<ConstraintViolation<Transfer>> constraintViolations =
                validator.validate(transfer);

        assertThat(constraintViolations.size(), is(1));
        assertThat(constraintViolations.iterator().next().getMessage(),
                is("Amount must be >= 0"));
    }


    @Test
    public void shouldHaveFromAccountNumber() {
        transfer.setAmount(0);
        transfer.setFromAccountNumber(null);
        transfer.setToAccountNumber(0);

        Set<ConstraintViolation<Transfer>> constraintViolations =
                validator.validate(transfer);

        assertThat(constraintViolations.size(), is(1));
        assertThat(constraintViolations.iterator().next().getMessage(),
                is("From account number must be set"));
    }


    @Test
    public void shouldHaveToAccountNumber() {
        transfer.setAmount(0);
        transfer.setFromAccountNumber(0);
        transfer.setToAccountNumber(null);

        Set<ConstraintViolation<Transfer>> constraintViolations =
                validator.validate(transfer);

        assertThat(constraintViolations.size(), is(1));
        assertThat(constraintViolations.iterator().next().getMessage(),
                is("To account number must be set"));
    }
}
