package com.qc.ali.codec;

import lombok.Getter;

public class CodecFailureException extends Exception {

    @Getter
    private String msg;

    public CodecFailureException(String error) {
        msg = error;
    }
}
