package com.qc.security.account.controller;

import com.qc.base.PaginationResponse;
import com.qc.base.QcBaseController;
import com.qc.base.R;
import com.qc.security.account.controller.res.DesensitiseAccount;
import com.qc.security.account.dto.Account;
import com.qc.security.account.exception.AccountNotFoundException;
import com.qc.security.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Objects;

@RestController
@RequestMapping("/api/qc/wework/account")
public class AccountController extends QcBaseController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/basic")
    public R<Account> getAccountByToken() throws AccountNotFoundException {
        String token = getToken();
        Account u = accountService.getAccountByToken(token);
        if (Objects.isNull(u)) {
            throw new AccountNotFoundException();
        }
        return R.suc(u);
//        return R.suc(desensitise(u));
    }

    @GetMapping("/list")
    public R<Collection<Account>> listAccounts(@RequestParam(name = "current", required = false, defaultValue = "1") int pageNum, @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize)  {
        PaginationResponse<Account> list = accountService.list(pageNum, pageSize);
        return R.suc(list);
    }

//    private DesensitiseAccount desensitise(Account account) {
//        DesensitiseAccount r = new DesensitiseAccount(account);
//        return r;
//    }

}