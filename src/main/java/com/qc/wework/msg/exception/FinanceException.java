package com.qc.wework.msg.exception;

import lombok.Getter;

public class FinanceException extends Exception {

    @Getter
    private int errCode = 0;

    public FinanceException(String message) {
        super(message);
    }

    public FinanceException(int errCode, String message) {
        this(message);
        this.errCode = errCode;
    }
}
