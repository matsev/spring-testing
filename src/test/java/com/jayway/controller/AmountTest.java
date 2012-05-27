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

public class AmountTest {

    static Validator validator;

    Amount amount;


    @BeforeClass
    public static void beforeClass() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }


    @Before
    public void setUp() throws Exception {
        amount = new Amount();
    }


    @Test
    public void shouldAllowZeroAmount() {
        amount.setAmount(0);

        Set<ConstraintViolation<Amount>> constraintViolations =
                validator.validate(amount);

        assertThat(constraintViolations.size(), is(0));
    }


    @Test
    public void shouldNotAllowNegativeAmount() {
        amount.setAmount(-1);

        Set<ConstraintViolation<Amount>> constraintViolations =
                validator.validate(amount);

        assertThat(constraintViolations.size(), is(1));
        assertThat(constraintViolations.iterator().next().getMessage(),
                is("Amount must be >= 0"));
    }
}
