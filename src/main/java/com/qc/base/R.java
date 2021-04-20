package com.qc.base;

import lombok.Data;

@Data
public class R<T> {

    private static final String DEFAULT_ERROR_CODE = "0";
    private static final String DEFAULT_ERROR_MSG = "";

    private boolean success;
    private String errorCode;
    private String errorMessage;
    private int showType;
    private String traceId;
    private String host;
    private T data;

    private R(boolean success, String errorCode, String msg) {
        this.success = success;
        this.errorCode = errorCode;
        this.errorMessage = msg;
    }

    public static <T> R suc() {
        R r = new R<T>(Boolean.TRUE, DEFAULT_ERROR_CODE, DEFAULT_ERROR_MSG);
        return r;
    }

    public static <T> R suc(T t) {
        R r = suc();
        r.setData(t);
        return r;
    }

    public static <T> R fail() {
        R r = new R<T>(Boolean.FALSE, DEFAULT_ERROR_CODE, DEFAULT_ERROR_MSG);
        return r;
    }

    public static <T> R fail(String code, String msg) {
        return new R<T>(Boolean.FALSE, code, msg);
    }
}
