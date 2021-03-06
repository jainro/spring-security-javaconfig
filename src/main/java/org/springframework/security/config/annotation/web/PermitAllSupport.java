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


import org.springframework.security.access.SecurityConfig;
import org.springframework.security.config.annotation.web.BaseUrlAuthorizationBuilder.UrlMapping;
import org.springframework.security.web.util.AntPathRequestMatcher;


/**
 *
 * @author Rob Winch
 * @since 3.2
 */
final class PermitAllSupport {

    public static void permitAll(SecurityFilterChainSecurityBuilder builder, String... urls) {
        DefaultSecurityFilterConfigurator configurator = builder.getConfigurator(DefaultSecurityFilterConfigurator.class);
        if(configurator != null) {
            BaseUrlAuthorizationBuilder fisBldr = configurator.filterInvocationSecurityMetadataSourceBuilder();
            if(!(fisBldr instanceof ExpressionUrlAuthorizationBuilder)) {
                throw new IllegalStateException(fisBldr + " is not supported with PermitAll use "+ fisBldr);
            }
            for(String url : urls) {
                fisBldr.addMapping(0, new UrlMapping(new AntPathRequestMatcher(url), SecurityConfig.createList(ExpressionUrlAuthorizationBuilder.permitAll)));
            }
        }
    }
}
