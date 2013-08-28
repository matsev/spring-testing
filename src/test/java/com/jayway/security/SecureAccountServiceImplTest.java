package com.jayway.security;

import com.jayway.domain.Account;
import com.jayway.service.AccountService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static java.util.Collections.singleton;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/application-context.xml", "/security-context.xml"})
@Transactional
@ActiveProfiles("h2")
public class SecureAccountServiceImplTest {

    @Autowired
    AccountService secureAccountService;

    UserDetails accountOwnerUser;
    UserDetails noAuthorityUser;


    @Before
    public void setUp() {
        accountOwnerUser = new User("unknown", "password", singleton(new SimpleGrantedAuthority("ACCOUNT_OWNER")));
        noAuthorityUser = new User("unknown", "password", Collections.<GrantedAuthority>emptySet());
    }

    
    @After
    public void tearDown() {
        SecurityContextHolder.clearContext();
    }


    @Test
    public void accountOwnerShouldGetAccount() {
        authenticate(accountOwnerUser);

        Account account = secureAccountService.get(1L);

        assertThat(account, notNullValue());
    }


    @Test(expected = AccessDeniedException.class)
    public void unAuthorizedShouldNotGetAccount() {
        authenticate(noAuthorityUser);

        secureAccountService.get(1L);
    }


    @Test
    public void accountOwnerShouldDeposit() {
        authenticate(accountOwnerUser);

        secureAccountService.deposit(1L, 100);
    }


    @Test(expected = AccessDeniedException.class)
    public void unAuthorizedShouldNotDeposit() {
        authenticate(noAuthorityUser);

        secureAccountService.deposit(1L, 100);
    }


    @Test
    public void accountOwnerShouldWithdraw() {
        authenticate(accountOwnerUser);

        Account account = secureAccountService.withdraw(1L, 50);

        assertThat(account, notNullValue());
    }


    @Test(expected = AccessDeniedException.class)
    public void unAuthorizedShouldNotWithdraw() {
        authenticate(noAuthorityUser);

        secureAccountService.withdraw(1L, 50);
    }


    @Test
    public void accountOwnerShouldTransfer() {
        authenticate(accountOwnerUser);

        secureAccountService.transfer(1L, 2L, 10);
    }


    @Test(expected = AccessDeniedException.class)
    public void unAuthorizedShouldNotTransfer() {
        authenticate(noAuthorityUser);

        secureAccountService.transfer(1L, 2L, 10);
    }


    @Test
    public void accountOwnerShouldGetAllAccountNumbers() {
        authenticate(accountOwnerUser);

        List<Long> allAccounts = secureAccountService.getAllAccountNumbers();

        assertThat(allAccounts, notNullValue());
    }


    @Test(expected = AccessDeniedException.class)
    public void unAuthorizedShouldGetAllAccountNumbers() {
        authenticate(noAuthorityUser);

        secureAccountService.getAllAccountNumbers();
    }


    private void authenticate(UserDetails userDetails) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                userDetails.getPassword(),
                userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
