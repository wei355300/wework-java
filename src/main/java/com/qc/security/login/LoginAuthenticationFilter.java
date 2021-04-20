package com.qc.security.login;

import com.qc.security.account.controller.req.LoginParams;
import com.qc.security.AccountDetail;
import com.qc.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public static final String USERNAME_KEY = "mobile";
    public static final String PASSWORD_KEY = "password";

    private LoginUserDetailService accountUserDetailService;

    public LoginAuthenticationFilter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager, LoginUserDetailService accountUserDetailService) {
        super(authenticationManager);
        setFilterProcessesUrl(defaultFilterProcessesUrl);
        setUsernameParameter(USERNAME_KEY);
        setPasswordParameter(PASSWORD_KEY);
        setAuthenticationSuccessHandler(new LoginAuthenticationSuccessHandler());
        setAuthenticationFailureHandler(new LoginAuthenticationFailureHandler());
        this.accountUserDetailService = accountUserDetailService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        String username = null;
        String password = null;
        LoginParams loginParams = null;

        String contentType = request.getContentType();

        if(StringUtils.isEmpty(contentType)) {
            throw new AuthenticationServiceException("Authentication content must not null");
        }

        if (MediaType.APPLICATION_JSON_VALUE.equals(contentType) || MediaType.APPLICATION_JSON_UTF8_VALUE.equals(contentType)) {
            try {
                loginParams = JsonUtils.parser(request, LoginParams.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        username = loginParams.getMobile();
        password = loginParams.getPassword();
        username = (username != null) ? username : "";
        username = username.trim();
        password = (password != null) ? password : "";
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        refreshToken(authResult);

        super.successfulAuthentication(request, response, chain, authResult);
    }

    private void refreshToken(Authentication authResult) {
        AccountDetail accountDetail = (AccountDetail) authResult.getPrincipal();
        String newToken = accountUserDetailService.generateToken(accountDetail.getTicket().getAccountId());
        accountDetail.getTicket().setToken(newToken);
    }
}
