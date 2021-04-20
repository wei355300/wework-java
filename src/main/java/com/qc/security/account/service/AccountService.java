package com.qc.security.account.service;

import com.qc.base.PaginationResponse;
import com.qc.security.account.dto.Account;

public interface AccountService {

    Account getAccountByMobile(String mobile);

    Account getAccountByToken(String token);

    String refreshAccountToken(Integer accountId);

    PaginationResponse<Account> list(int pageNum, int pageSize);
}
