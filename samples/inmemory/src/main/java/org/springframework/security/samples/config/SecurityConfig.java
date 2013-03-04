package org.springframework.security.samples.config;

import static org.springframework.security.config.annotation.SecurityExpressions.hasRole;
import static org.springframework.security.config.annotation.SecurityExpressions.permitAll;
import static org.springframework.security.config.annotation.authentication.AuthenticationSecurityBuilders.*;
import static org.springframework.security.config.annotation.web.FilterInvocationSecurityMetadataSourceSecurityBuilder.antMatchers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.provisioning.InMemoryUserDetailsManagerSecurityBuilder;
import org.springframework.security.config.annotation.web.DefaultSecurityFilterConfigurator;
import org.springframework.security.config.annotation.web.EnableWebSecurity;
import org.springframework.security.config.annotation.web.FilterChainProxySecurityBuilder;
import org.springframework.security.config.annotation.web.FilterInvocationSecurityMetadataSourceSecurityBuilder;
import org.springframework.security.config.annotation.web.FormLoginSecurityFilterConfigurator;
import org.springframework.security.config.annotation.web.SecurityFilterChainSecurityBuilder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity
public class SecurityConfig {

    @Bean
    public AuthenticationManager authenticationMgr() throws Exception {
        return authenticationManager(userDetailsManager()).build();
    }

    @Bean
    public InMemoryUserDetailsManagerSecurityBuilder userDetailsManager() throws Exception {
        return inMemoryAuthentication(
          user("user").password("password").roles("USER"),
          user("admin").password("password").roles("USER", "ADMIN"));
    }

    @Bean
    public FilterChainProxySecurityBuilder builder() throws Exception {
        FilterInvocationSecurityMetadataSourceSecurityBuilder fiSourceBldr = new FilterInvocationSecurityMetadataSourceSecurityBuilder()
            // TODO type safe configAttributes
            .interceptUrl(antMatchers("/users**","/sessions/**"), hasRole("ADMIN"))
            .interceptUrl(antMatchers("/resources/**","/signup"), permitAll)
            .antInterceptUrl("/**", hasRole("USER"));

        return new FilterChainProxySecurityBuilder()
            .ignoring(antMatchers("/resources/**"))
            .securityFilterChains(
                new SecurityFilterChainSecurityBuilder(authenticationMgr())
                    .apply(new DefaultSecurityFilterConfigurator(fiSourceBldr).permitAll())
                    .apply(new FormLoginSecurityFilterConfigurator().permitAll()));
    }
}