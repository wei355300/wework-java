package com.qc.security;

import com.qc.security.account.dto.Account;
import com.qc.security.account.service.AccountService;
import com.qc.security.login.LoginTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ApiRequestUserDetailService implements UserDetailsService {

    @Autowired
    private AccountService accountService;

    @Override
    public UserDetails loadUserByUsername(String token) throws UsernameNotFoundException {

        Account account = accountService.getAccountByToken(token);
        if (Objects.isNull(account)) {
            throw new UsernameNotFoundException(token);
        }
        AccountDetail detail = new AccountDetail(account.getMobile(), account.getPassword(), AccountDetail.toAuthorities(account.getAuthority()));
        detail.setTicket(new LoginTicket(account));

        return detail;
    }
}
