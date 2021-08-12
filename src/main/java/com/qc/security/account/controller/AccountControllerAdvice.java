package com.qc.security.account.controller;

import com.qc.base.R;
import com.qc.security.account.exception.AccountExistException;
import com.qc.security.account.exception.AccountNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class AccountControllerAdvice {

    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseBody
    public R handleAccountNotFoundException() {
        return R.fail("10001", "账号不存在");
    }

    @ExceptionHandler(AccountExistException.class)
    @ResponseBody
    public R handleAccountExistException() {
        return R.fail("10002", "账号已存在");
    }
}
