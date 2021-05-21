package com.qc.wework.msg.controller;

import com.qc.base.R;
import com.qc.wework.msg.exception.MsgException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class MsgControllerAdvice {

    @ExceptionHandler(MsgException.class)
    @ResponseBody
    public R handleAccountNotFoundException() {
        return R.fail("10001", "账号不存在");
    }
}
