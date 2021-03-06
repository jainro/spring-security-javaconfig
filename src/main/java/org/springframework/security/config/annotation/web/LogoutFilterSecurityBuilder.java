package org.springframework.security.config.annotation.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

// TODO should be configurator
public class LogoutFilterSecurityBuilder implements SecurityBuilder<LogoutFilter> {
    private List<LogoutHandler> logoutHandlers = new ArrayList<LogoutHandler>();
    private SecurityContextLogoutHandler contextLogoutHandler = new SecurityContextLogoutHandler();
    private String logoutSuccessUrl = "/login?logout";
    private LogoutSuccessHandler logoutSuccessHandler;
    private String logoutUrl = "/logout";

    @Override
    public LogoutFilter build() throws Exception {
        logoutHandlers.add(contextLogoutHandler);
        LogoutHandler[] handlers = logoutHandlers.toArray(new LogoutHandler[logoutHandlers.size()]);
        LogoutFilter result = new LogoutFilter(getLogoutSuccessHandler(), handlers);
        result.setFilterProcessesUrl(logoutUrl);
        result.afterPropertiesSet();
        return result;
    }

    public LogoutFilterSecurityBuilder addLogoutHandler(LogoutHandler logoutHandler) {
        this.logoutHandlers.add(logoutHandler);
        return this;
    }

    public LogoutFilterSecurityBuilder invalidateHttpSession(boolean invalidateHttpSession) {
        contextLogoutHandler.setInvalidateHttpSession(invalidateHttpSession);
        return this;
    }

    public LogoutFilterSecurityBuilder logoutUrl(String logoutUrl) {
        this.logoutUrl = logoutUrl;
        return this;
    }

    public LogoutFilterSecurityBuilder logoutSuccessUrl(String logoutSuccessUrl) {
        this.logoutSuccessUrl = logoutSuccessUrl;
        return this;
    }

    public LogoutFilterSecurityBuilder logoutSuccessHandler(LogoutSuccessHandler logoutSuccessHandler) {
        this.logoutSuccessHandler = logoutSuccessHandler;
        return this;
    }

    private LogoutSuccessHandler getLogoutSuccessHandler() {
        if(logoutSuccessHandler != null) {
            return logoutSuccessHandler;
        }
        SimpleUrlLogoutSuccessHandler logoutSuccessHandler = new SimpleUrlLogoutSuccessHandler();
        logoutSuccessHandler.setDefaultTargetUrl(logoutSuccessUrl);
        return logoutSuccessHandler;
    }

    public LogoutFilterSecurityBuilder deleteCookies(String... cookieNamesToClear) {
        return addLogoutHandler(new CookieClearingLogoutHandler(cookieNamesToClear));
    }

    String getLogoutSuccesUrl() {
        return logoutSuccessHandler == null ? logoutSuccessUrl : null;
    }

    String getLogoutUrl() {
        return logoutUrl;
    }
}