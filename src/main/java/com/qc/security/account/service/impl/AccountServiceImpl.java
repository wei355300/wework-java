package com.qc.security.account.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qc.base.PaginationResponse;
import com.qc.security.account.dto.Account;
import com.qc.security.account.mapper.AccountMapper;
import com.qc.security.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public Account getAccountByMobile(String mobile) {
        Account account = accountMapper.getByMobile(mobile);
        return account;
    }

    @Override
    public Account getAccountByToken(String token) {
        return accountMapper.getByToken(token);
    }

    @Transactional
    @Override
    public String refreshAccountToken(Integer accountId) {
        if (accountId == null) {
            throw new NullPointerException();
        }
        String uuid = UUID.randomUUID().toString();
        accountMapper.updateTokenByAccount(accountId, uuid);
        return uuid;
    }

    @Override
    public PaginationResponse<Account> list(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Account> list = accountMapper.list();
        PageInfo page = new PageInfo(list);
        return PaginationResponse.toPagination(page);
    }

    @Transactional
    @Override
    public void updatePassword(String mobile, String newPass) {
        accountMapper.updatePasswordByMobile(mobile, newPass);
        disableToken(mobile);

    }

    @Override
    public void delAccount(String mobile) {
        accountMapper.delAccount(mobile);
    }

    private void disableToken(String mobile) {
        accountMapper.updateTokenByMobile(mobile,"");
    }
}
