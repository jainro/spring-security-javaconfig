Spring Security Java Config
======================

A project for Spring Security Java Configuration Support. We plan to release this jar in a number of milestones
to be able to iterate quickly. Additionally, this will allow users of Spring Security 3.1.x to use the
Java Configuration earlier and give more feedback. Eventually (targeting spring-security-config-3.2.0.RELEASE)
we will merge this code in with spring-security-config.

Building with Maven
==============

The project is available in the [Spring Snapshot Repository](https://github.com/SpringSource/spring-framework/wiki/SpringSource-repository-FAQ).
In short ensure you have the following repository in your pom.xml:

    <repository>
        <id>spring-libs-snapshot</id>
        <name>Spring Snapshot Repository</name>
        <url>http://repo.springsource.org/snapshot</url>
        <releases>
            <enabled>false</enabled>
        </releases>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>

You will then need to include the Spring Security Java Configuration jar.

    <dependencies>
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-javaconfig</artifactId>
            <version>1.0.0.CI-SNAPSHOT</version>
        </dependency>
    </dependencies>


Getting Started
======================

Below are a few things you can do to get up and running quickly.

Sample Web Configuration
----------------------

The following configuration

    import static org.springframework.security.config.annotation.authentication.AuthenticationSecurityBuilders.*;
    import static org.springframework.security.config.annotation.web.WebSecurityConfigurators.*;
    import static org.springframework.security.config.annotation.web.util.RequestMatchers.*;

    @Configuration
    public class SampleSimpleWebSecurityConfig extends SimpleWebSecurityConfig {
        protected void authorizeUrls(ExpressionUrlAuthorizationBuilder interceptUrls) {
            interceptUrls
                .antMatchers("/signup","/about").permitAll()
                .antMatchers("/**").hasRole("USER");
        }

        protected void configure(
                SecurityFilterChainSecurityBuilder springSecurityFilterChain) {
            springSecurityFilterChain
                .apply(formLogin()
                    // ensure the URLs for login are publicly accessible
                    .permitAll());
        }

        protected AuthenticationManager authenticationMgr() throws Exception {
            return inMemoryAuthentication(
                user("user").password("password").roles("USER"),
                user("admin").password("password").roles("USER", "ADMIN")
            ).authenticationManager();
        }
    }

is similar to the following XML configuration:

    <http use-expressions="true">
      <intercept-url pattern="/logout" access="permitAll"/>
      <intercept-url pattern="/login" access="permitAll"/>
      <intercept-url pattern="/signup" access="permitAll"/>
      <intercept-url pattern="/about" access="permitAll"/>
      <intercept-url pattern="/**" access="hasRole('ROLE_USER')"/>
      <logout
          logout-success-url="/login?logout"
          logout-url="/logout"
      <form-login
          authentication-failure-url="/login?error"
          login-page="/login"
          login-processing-url="/login" <!-- but only POST -->
          password-parameter="password"
          username-parameter="username"
      />
    </http>
    <authentication-manager>
      <authentication-provider>
        <user-service>
          <user username="user" password="password" authorities="ROLE_USER"/>
          <user username="admin" password="password" authorities="ROLE_USER,ROLE_ADMIN"/>
        </user-service>
      </authentication-provider>
    </authentication-manager>

Notice that Spring Security uses different defaults that will make your HTTP requests appear more RESTful. For example, the URL /login POST is used to
authenticate users. The URL /login GET is used to request the user for credentials (i.e. present a login form).

Sample Global Security Configuration
-------------

Global configuration is quite simple. For example, the following Java Configuration:

    @Configuration
    @EnableGlobalMethodSecurity(prePostEnabled=true)
    public class SampleWebSecurityConfig {
        @Bean
        public MethodSecurityService methodSecurityService() {
            return new MethodSecurityServiceImpl()
        }

        @Bean
        public AuthenticationManager authenticationMgr() throws Exception {
            return inMemoryAuthentication(
                user("user").password("password").roles("USER"),
                user("admin").password("password").roles("USER", "ADMIN")
            ).authenticationManager();
        }
    }

is the equivalent of:

    <global-method-security pre-post-annotations="enabled"/>
    <authentication-manager>
      <authentication-provider>
        <user-service>
          <user username="user" password="password" authorities="ROLE_USER"/>
          <user username="admin" password="password" authorities="ROLE_USER,ROLE_ADMIN"/>
        </user-service>
      </authentication-provider>
    </authentication-manager>
    <beans:bean id="methodSecuriytService" class="MethodSecurityServiceImpl"/>

There are additional attributes on `EnableGlobalMethodSecurity`, but in more advanced situations you may want to refer to another object. In order to do this,
override the `GlobalMethodSecurityConfiguration` class. For example, following Java configuration demonstrates how to override the MethodExpressionHandler to use
`CustomPermissionEvaluator`.

    @Configuration
    @EnableGlobalMethodSecurity(prePostEnabled=true)
    public class CustomPermissionEvaluatorWebSecurityConfig extends GlobalMethodSecurityConfiguration {
        @Bean
        public MethodSecurityService methodSecurityService() {
            return new MethodSecurityServiceImpl()
        }

        @Override
        protected MethodSecurityExpressionHandler expressionHandler() {
            DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
            expressionHandler.setPermissionEvaluator(new CustomPermissionEvaluator());
            return expressionHandler;
        }

        @Bean
        public AuthenticationManager authenticationMgr() throws Exception {
            return inMemoryAuthentication(
                user("user").password("password").roles("USER"),
                user("admin").password("password").roles("USER", "ADMIN")
            ).authenticationManager();
        }
    }

The configuration above is the similar to the following XML configuration:

    <global-method-security pre-post-annotations="enabled">
        <expression-handler ref="expressionHandler"/>
    </global-method-security>
    <authentication-manager>
      <authentication-provider>
        <user-service>
          <user username="user" password="password" authorities="ROLE_USER"/>
          <user username="admin" password="password" authorities="ROLE_USER,ROLE_ADMIN"/>
        </user-service>
      </authentication-provider>
    </authentication-manager>
    <beans:bean id="methodSecuriytService" class="MethodSecurityServiceImpl"/>
    <beans:bean id="expressionHandler" class="CustomExpressionHandler"/>

Additional Samples
-------------

We include a number of complete [Sample Web Applications](./samples/) that use Spring Security Java Configuration. Also refer to the tests
for further examples. You will notice a convention of Namespace<Security Element>Tests where <Security Element> is the Security Namespace
Element. For example, to learn how the <http> element and its attributes map to Spring Security Java Configuration look in the NamespaceHttpTests

Contributing
==============
Before contributing or logging an issue please be sure to the issue does not already exist in this project's [issue tracking](https://github.com/SpringSource/spring-security-javaconfig/issues). If one does not exist, please create an issue.

If you see anything you'd like to change we encourage taking advantage of github's social coding features by making the change in a [fork of this repository](http://help.github.com/forking/) and sending a pull request.

Before we accept a non-trivial patch or pull request we will need you to sign the [contributor's agreement](https://support.springsource.com/spring_committer_signup). Signing the contributor's agreement does not grant anyone commit rights to the main repository, but it does mean that we can accept your contributions, and you will get an author credit if we do. Active contributors might be asked to join the core team, and given the ability to merge pull requests.

License
==============
The Spring Security Java Config project is available under version 2.0 of the [Apache License](http://www.apache.org/licenses/LICENSE-2.0).
