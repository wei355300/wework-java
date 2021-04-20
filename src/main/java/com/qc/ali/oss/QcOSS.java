package com.qc.ali.oss;

public abstract class QcOSS {

    /**
     * 使用追加上传
     * @param name
     * @param content
     * @return
     */
    public boolean uploadAppend(String name, byte[] content) {
        return false;
    }
}
