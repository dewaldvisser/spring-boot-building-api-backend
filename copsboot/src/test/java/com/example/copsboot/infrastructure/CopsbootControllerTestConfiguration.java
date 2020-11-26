package com.example.copsboot.infrastructure;

import com.example.copsboot.security.OAuth2ServerConfiguration;
import com.example.copsboot.security.SecurityConfiguration;
import com.example.copsboot.security.StubUserDetailsService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;

@TestConfiguration
@Import(OAuth2ServerConfiguration.class)
public class CopsbootControllerTestConfiguration {
    @Bean
    public UserDetailsService userDetailsService() {
        return new StubUserDetailsService();
    }

    @Bean
    public SecurityConfiguration securityConfiguration() {
        return new SecurityConfiguration();
    }

}
