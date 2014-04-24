package com.jayway.security;

import com.jayway.config.ApplicationConfig;
import com.jayway.config.SecurityConfig;
import com.jayway.domain.Account;
import com.jayway.service.AccountService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ApplicationConfig.class, SecurityConfig.class})
// @ContextConfiguration({"/application-context.xml", "/security-context.xml"})
@ActiveProfiles("h2")
@Transactional
public class SecureAccountServiceImplTest {

    @Autowired
    AccountService secureAccountService;


    @Before
    public void setUp() {
        SecurityContextHolder.clearContext();
    }


    @Test
    public void accountOwnerShouldGetAccount() {
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ACCOUNT_OWNER");
        authenticateWithAuthorities(authorities);

        Account account = secureAccountService.get(1L);

        assertThat(account, notNullValue());
    }


    @Test(expected = AccessDeniedException.class)
    public void unAuthorizedShouldNotGetAccount() {
        authenticateWithAuthorities(AuthorityUtils.NO_AUTHORITIES);

        secureAccountService.get(1L);
    }


    @Test
    public void accountOwnerShouldDeposit() {
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ACCOUNT_OWNER");
        authenticateWithAuthorities(authorities);

        secureAccountService.deposit(1L, 100);
    }


    @Test(expected = AccessDeniedException.class)
    public void unAuthorizedShouldNotDeposit() {
        authenticateWithAuthorities(AuthorityUtils.NO_AUTHORITIES);

        secureAccountService.deposit(1L, 100);
    }


    @Test
    public void accountOwnerShouldWithdraw() {
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ACCOUNT_OWNER");
        authenticateWithAuthorities(authorities);

        Account account = secureAccountService.withdraw(1L, 50);

        assertThat(account, notNullValue());
    }


    @Test(expected = AccessDeniedException.class)
    public void unAuthorizedShouldNotWithdraw() {
        authenticateWithAuthorities(AuthorityUtils.NO_AUTHORITIES);

        secureAccountService.withdraw(1L, 50);
    }


    @Test
    public void accountOwnerShouldTransfer() {
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ACCOUNT_OWNER");
        authenticateWithAuthorities(authorities);

        secureAccountService.transfer(1L, 2L, 10);
    }


    @Test(expected = AccessDeniedException.class)
    public void unAuthorizedShouldNotTransfer() {
        authenticateWithAuthorities(AuthorityUtils.NO_AUTHORITIES);

        secureAccountService.transfer(1L, 2L, 10);
    }


    @Test
    public void accountOwnerShouldGetAllAccountNumbers() {
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ACCOUNT_OWNER");
        authenticateWithAuthorities(authorities);

        List<Long> allAccounts = secureAccountService.getAllAccountNumbers();

        assertThat(allAccounts, notNullValue());
    }


    @Test(expected = AccessDeniedException.class)
    public void unAuthorizedShouldGetAllAccountNumbers() {
        authenticateWithAuthorities(AuthorityUtils.NO_AUTHORITIES);

        secureAccountService.getAllAccountNumbers();
    }


    private void authenticateWithAuthorities(List<GrantedAuthority> authorities) {
        TestingAuthenticationToken authenticationToken = new TestingAuthenticationToken("name", "password", authorities);
        authenticationToken.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
