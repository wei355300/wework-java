package com.qc.wework.msg.controller;

import com.qc.ali.codec.TransferFailureException;
import com.qc.base.R;
import com.qc.wework.msg.exception.MsgException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class MsgControllerAdvice {

    @ExceptionHandler(MsgException.class)
    @ResponseBody
    public R handleMsgException() {
        return R.fail("20001", "获取消息失败, 稍后重试!");
    }

    @ExceptionHandler(TransferFailureException.class)
    @ResponseBody
    public R handleCodecFailedException() {
        return R.fail("20002", "转码失败, 稍后重试!");
    }
}
