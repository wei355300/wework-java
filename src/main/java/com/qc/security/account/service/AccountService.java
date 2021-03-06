package com.qc.security.account.service;

import com.qc.base.PaginationResponse;
import com.qc.security.account.dto.Account;
import com.qc.security.account.exception.AccountExistException;

public interface AccountService {

    Account getAccountByMobile(String mobile);

    Account getAccountByToken(String token);

    String refreshAccountToken(Integer accountId);

    PaginationResponse<Account> list(int pageNum, int pageSize);

    void openAccount(String mobile, String pass, String authority) throws AccountExistException;

    void updatePassword(String mobile, String newPass);

    void delAccount(String mobile);
}
