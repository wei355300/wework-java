package com.qc.security.login;

import com.qc.security.AccountDetail;
import com.qc.security.account.dto.Account;
import com.qc.security.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class LoginUserDetailService implements UserDetailsService {

    @Autowired
    private AccountService accountService;

    @Override
    public UserDetails loadUserByUsername(String mobile) throws UsernameNotFoundException {
        Account account = accountService.getAccountByMobile(mobile);

        if (Objects.isNull(account)) {
            throw new UsernameNotFoundException(mobile);
        }

        AccountDetail detail = new AccountDetail(account.getMobile(), account.getPassword(), AccountDetail.toAuthorities(account.getAuthority()));
        detail.setTicket(new LoginTicket(account));

        return detail;
    }

    public String generateToken(Integer accountId) {
        String newToken = accountService.refreshAccountToken(accountId);
        return newToken;
    }
}
