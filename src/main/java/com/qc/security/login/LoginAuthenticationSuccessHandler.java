package com.qc.security.login;

import com.qc.base.R;
import com.qc.security.account.dto.Account;
import com.qc.security.AccountDetail;
import com.qc.utils.JsonUtils;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            String contentType = request.getContentType();
            if (MediaType.APPLICATION_JSON_VALUE.equals(contentType) || MediaType.APPLICATION_JSON_UTF8_VALUE.equals(contentType)) {
                AccountDetail accountDetail = (AccountDetail) authentication.getPrincipal();
                LoginTicket ticket = accountDetail.getTicket();
                Account user = new Account();
                user.setAuthority(ticket.getAuthority());
                user.setToken(ticket.getToken());
//                user.setName(ticket.get.getName());
//                user.setMobile(account.getMobile());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.getWriter().write(JsonUtils.toJson(R.suc(user)));
            }
        }
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
