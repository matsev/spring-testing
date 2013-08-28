package com.jayway.security;

import org.springframework.context.annotation.Profile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

import static java.util.Collections.singleton;

@Profile({"h2", "mysql"})
@Service
class InMemoryUserDetailsService extends InMemoryUserDetailsManager {

    public InMemoryUserDetailsService() {
        super(singleton((UserDetails)
                new User("user", "secret", singleton(new SimpleGrantedAuthority("ACCOUNT_OWNER")))
        ));
    }

}
