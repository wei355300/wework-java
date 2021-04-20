package com.qc.security.account.controller;

import com.qc.base.R;
import com.qc.security.account.exception.AccountNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class AccountControllerAdvice {

//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseBody
    public R handleUserException() {
        return R.fail("10001", "account not found");
    }
}
