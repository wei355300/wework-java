package com.qc.base;

import com.qc.security.ApiRequestAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class QcBaseController {

    public static String getToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof ApiRequestAuthenticationToken) {
            return ((ApiRequestAuthenticationToken) authentication).getToken();
        }
        throw new BadCredentialsException("maybe didn't login");
    }
}
