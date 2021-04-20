package com.qc.base;

import lombok.Getter;

public class QcBaseException extends Exception {


    @Getter
    private String errCode;

    public QcBaseException(String errCode, String message) {
        super(message);
        this.errCode = errCode;
    }
}
