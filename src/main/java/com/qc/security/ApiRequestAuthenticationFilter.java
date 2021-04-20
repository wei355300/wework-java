package com.qc.security;

import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class ApiRequestAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public static final String SPRING_SECURITY_FORM_TOKEN_KEY = "Token";


    private String tokenParameter = SPRING_SECURITY_FORM_TOKEN_KEY;

    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/api/**");

    private static AuthenticationFailureHandler failureHandler = new AuthenticationEntryPointFailureHandler(new Http403ForbiddenEntryPoint());

    public ApiRequestAuthenticationFilter() {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
    }

    protected ApiRequestAuthenticationFilter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager) {
        super(defaultFilterProcessesUrl, authenticationManager);
        setAuthenticationFailureHandler(failureHandler);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (!requiresAuthentication(request, response)) {
            chain.doFilter(request, response);
            return;
        }
        try {
            Authentication authenticationResult = attemptAuthentication(request, response);
            if (authenticationResult == null) {
                // return immediately as subclass has indicated that it hasn't completed
                return;
            }
//            this.sessionStrategy.onAuthentication(authenticationResult, request, response);
            // Authentication success
//            if (this.continueChainBeforeSuccessfulAuthentication) {
//                chain.doFilter(request, response);
//            }
            successfulAuthentication(request, response, chain, authenticationResult);
            chain.doFilter(request, response);
        } catch (InternalAuthenticationServiceException failed) {
            this.logger.error("An internal error occurred while trying to authenticate the account.", failed);
            unsuccessfulAuthentication(request, response, failed);
        } catch (AuthenticationException ex) {
            // Authentication failed
            unsuccessfulAuthentication(request, response, ex);
        }
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
//        String uri = request.getRequestURI();
//        if (!uri.startsWith("/api")) {
//            throw new AuthenticationServiceException("Authentication resource not supported: " + request.getRequestURI());
//        }
        String token = obtainToken(request);
        token = (token != null) ? token : "";
        token = token.trim();
        ApiRequestAuthenticationToken authRequest = new ApiRequestAuthenticationToken(token);
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
    }


    @Nullable
    String obtainToken(HttpServletRequest request) {
        String token = request.getHeader(this.tokenParameter);
        if (Objects.isNull(token)) {
            token = request.getParameter(this.tokenParameter);
        }
        return token;
    }

    void setDetails(HttpServletRequest request, ApiRequestAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    public void setTokenParameter(String tokenParameter) {
        Assert.hasText(tokenParameter, "token parameter must not be empty or null");
        this.tokenParameter = tokenParameter;
    }

    public final String getTokenParameter() {
        return this.tokenParameter;
    }
}

