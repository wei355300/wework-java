package com.qc.security.account.controller;

import com.qc.base.PaginationResponse;
import com.qc.base.QcBaseController;
import com.qc.base.R;
import com.qc.security.account.controller.req.DelAccountParams;
import com.qc.security.account.controller.req.OpenAccountParams;
import com.qc.security.account.controller.req.UpdatePasswordParams;
import com.qc.security.account.dto.Account;
import com.qc.security.account.exception.AccountExistException;
import com.qc.security.account.exception.AccountNotFoundException;
import com.qc.security.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    }

    @GetMapping("/list")
    public R<Collection<Account>> listAccounts(@RequestParam(name = "current", required = false, defaultValue = "1") int pageNum, @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize) {
        PaginationResponse<Account> list = accountService.list(pageNum, pageSize);
        return R.suc(list);
    }

    @PutMapping("/update/pwd")
    public R updatePassword(@RequestBody @Valid UpdatePasswordParams params) {
        if (!params.getNewPass().equals(params.getNewPassConfirm())) {
            return R.fail("1100", "密码不一致");
        }
        accountService.updatePassword(params.getMobile(), params.getNewPass());
        return R.suc();
    }

    @PostMapping("/open")
    public R openAccount(@RequestBody OpenAccountParams params) throws AccountExistException {
        accountService.openAccount(params.getMobile(), params.getPassword(), "admin");
        return R.suc();
    }

    @PostMapping("/del")
    public R del(@RequestBody DelAccountParams params) {
        accountService.delAccount(params.getMobile());
        return R.suc();
    }

//    private DesensitiseAccount desensitise(Account account) {
//        DesensitiseAccount r = new DesensitiseAccount(account);
//        return r;
//    }

}
