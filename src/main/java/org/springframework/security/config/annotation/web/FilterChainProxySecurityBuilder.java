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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.util.RequestMatcher;
import org.springframework.util.Assert;

/**
 *
 * @author Rob Winch
 * @since 3.2
 */
public class FilterChainProxySecurityBuilder implements SecurityBuilder<FilterChainProxy> {
    private List<RequestMatcher> ignoredRequests = new ArrayList<RequestMatcher>();
    private List<SecurityFilterChainSecurityBuilder> filterChains = new ArrayList<SecurityFilterChainSecurityBuilder>();
    private FilterSecurityInterceptor filterSecurityInterceptor;
    private HttpFirewall httpFirewall;

    // TODO change this to SecurityBuilder<SecurityFilterChain> when we eliminate the need for creating a global WebInvocationPrivilegeEvaluator
    public FilterChainProxySecurityBuilder securityFilterChains(SecurityFilterChainSecurityBuilder... securityFilterChainBuilders) {
        filterChains = Arrays.asList(securityFilterChainBuilders);
        return this;
    }

    public FilterChainProxy build() throws Exception {
        Assert.state(!filterChains.isEmpty(), "At least one SecurityFilterBuilder needs to be specified. Invoke FilterChainProxyBuilder.securityFilterChains");
        int chainSize = ignoredRequests.size() + filterChains.size();
        List<SecurityFilterChain> securityFilterChains = new ArrayList<SecurityFilterChain>(chainSize);
        for(RequestMatcher ignoredRequest : ignoredRequests) {
            securityFilterChains.add(new DefaultSecurityFilterChain(ignoredRequest));
        }
        for(SecurityFilterChainSecurityBuilder builder : filterChains) {
            securityFilterChains.add(builder.build());
            this.filterSecurityInterceptor = builder.getSharedObject(FilterSecurityInterceptor.class);
        }
        FilterChainProxy filterChainProxy = new FilterChainProxy(securityFilterChains);
        if(httpFirewall != null) {
            filterChainProxy.setFirewall(httpFirewall);
        }
        return filterChainProxy;
    }

    public FilterChainProxySecurityBuilder httpFirewall(HttpFirewall httpFirewall) {
        this.httpFirewall = httpFirewall;
        return this;
    }

    public FilterChainProxySecurityBuilder ignoring(List<? extends RequestMatcher> requestsToIgnore) {
        this.ignoredRequests = new ArrayList<RequestMatcher>(requestsToIgnore);
        return this;
    }

    public FilterSecurityInterceptor securityInterceptor() {
        return filterSecurityInterceptor;
    }

}
