package com.qc.base;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class QcBaseControllerAdvice {

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(QcBaseException.class)
    @ResponseBody
    public R handleUserException(QcBaseException e) {
        return R.fail(e.getErrCode(), e.getMessage());
    }
}
