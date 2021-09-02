package com.qc.ali.codec;

import lombok.Getter;

public class TransferFailureException extends Exception {

    @Getter
    private String msg;

    public TransferFailureException(String error) {
        msg = error;
    }
}
