package com.qc.security.login;

import com.qc.security.account.dto.Account;
import lombok.Data;

@Data
public class LoginTicket {
    private Integer accountId;
    private String token;
    private String authority;

    public LoginTicket() {
    }

    public LoginTicket(Account account) {
        this.accountId = account.getId();
        this.token = account.getToken();
        this.authority = account.getAuthority();
    }
}
