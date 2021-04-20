package com.qc.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component("apiRequestAuthorizationChecker")
public class ApiRequestAuthorizationChecker {

    public boolean check(Authentication authentication, HttpServletRequest request) {
        return true;
    }
}
