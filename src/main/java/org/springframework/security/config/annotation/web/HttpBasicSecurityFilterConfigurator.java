/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.springframework.security.config.annotation.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurator;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 *
 * @author Rob Winch
 * @since 3.2
 */
public class HttpBasicSecurityFilterConfigurator implements SecurityConfigurator<SecurityFilterChainSecurityBuilder> {
    private BasicAuthenticationFilter basicAuthenticationFilter;
    private AuthenticationEntryPoint authenticationEntryPoint;
    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource;

    public HttpBasicSecurityFilterConfigurator() throws Exception {
        realmName("Spring Security Application");
    }

    public void configure(SecurityFilterChainSecurityBuilder builder) throws Exception {
        AuthenticationManager authenticationManager = builder.authenticationManager();
        basicAuthenticationFilter = new BasicAuthenticationFilter(authenticationManager, authenticationEntryPoint);
        if(authenticationDetailsSource != null) {
            basicAuthenticationFilter.setAuthenticationDetailsSource(authenticationDetailsSource);
        }
        basicAuthenticationFilter.afterPropertiesSet();
        builder.addFilter(basicAuthenticationFilter);
    }

    public HttpBasicSecurityFilterConfigurator realmName(String realmName) throws Exception {
        BasicAuthenticationEntryPoint basicAuthEntryPoint = new BasicAuthenticationEntryPoint();
        basicAuthEntryPoint.setRealmName(realmName);
        basicAuthEntryPoint.afterPropertiesSet();
        return authenticationEntryPoint(basicAuthEntryPoint);
    }

    public HttpBasicSecurityFilterConfigurator authenticationEntryPoint(AuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        return this;
    }

    public HttpBasicSecurityFilterConfigurator authenticationDetailsSource(AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource) {
        this.authenticationDetailsSource = authenticationDetailsSource;
        return this;
    }

    @Override
    public void init(SecurityFilterChainSecurityBuilder builder)
            throws Exception {
    }
}
