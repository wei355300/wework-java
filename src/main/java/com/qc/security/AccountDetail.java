package com.qc.security;

import com.qc.security.login.LoginTicket;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Collection;

public class AccountDetail extends User {

    @Getter
    @Setter
    private LoginTicket ticket;

    public AccountDetail(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public static Collection<GrantedAuthority> toAuthorities(String authority) {
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        GrantedAuthority a = new SimpleGrantedAuthority(authority);
        authorities.add(a);
        return authorities;
    }

    public void setTicket(LoginTicket ticket) {
        this.ticket = ticket;
    }

//    public void setTicket(Integer accountId, String token, String authority) {
//        this.ticket = new LoginTicket();
//        ticket.setAccountId(accountId);
//        ticket.setToken(token);
//        ticket.setAuthority(authority);
//    }
}
