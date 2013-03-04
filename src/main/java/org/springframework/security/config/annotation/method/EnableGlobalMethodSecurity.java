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
package org.springframework.security.config.annotation.method;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.security.access.annotation.Secured;

/**
 * <p>Enables Spring Security global method security similar to the
 * <global-method-security> xml support.</p>
 *
 * <p>
 * More advanced configurations may wish to extend
 * {@link GlobalMethodSecurityConfiguration} and override the protected methods
 * to provide custom implementations. Note that
 * {@link EnableGlobalMethodSecurity} still must be included on the class
 * extending {@link GlobalMethodSecurityConfiguration} to determine the
 * settings.
 *
 * @author Rob Winch
 * @since 3.2
 */
@Retention(value=java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value={java.lang.annotation.ElementType.TYPE})
@Documented
@Import(GlobalMethodSecuritySelector.class)
public @interface EnableGlobalMethodSecurity {

    /**
     * Determines if Spring Security's pre post annotations should be enabled.
     * @return
     */
    boolean prePostEnabled() default false;

    /**
     * Determines if Spring Security's {@link Secured} annotations should be enabled.
     * @return
     */
    boolean securedEnabled() default false;

    /**
     * Determines if JSR-250 annotations should be enabled.
     * @return
     */
    boolean jsr250Enabled() default false;

    /**
     * Indicate whether subclass-based (CGLIB) proxies are to be created ({@code true}) as
     * opposed to standard Java interface-based proxies ({@code false}). The default is
     * {@code false}. <strong>Applicable only if {@link #mode()} is set to
     * {@link AdviceMode#PROXY}</strong>.
     *
     * <p>Note that setting this attribute to {@code true} will affect <em>all</em>
     * Spring-managed beans requiring proxying, not just those marked with
     * the Security annotations. For example, other beans marked with Spring's
     * {@code @Transactional} annotation will be upgraded to subclass proxying at the same
     * time. This approach has no negative impact in practice unless one is explicitly
     * expecting one type of proxy vs another, e.g. in tests.
     */
    boolean proxyTargetClass() default false;

    /**
     * Indicate how security advice should be applied. The default is
     * {@link AdviceMode#PROXY}.
     * @see AdviceMode
     */
    AdviceMode mode() default AdviceMode.PROXY;

    /**
     * Indicate the ordering of the execution of the security advisor
     * when multiple advices are applied at a specific joinpoint.
     * The default is {@link Ordered#LOWEST_PRECEDENCE}.
     */
    int order() default Ordered.LOWEST_PRECEDENCE;
}