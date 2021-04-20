package com.qc.ali.oss;

import lombok.Data;

import java.util.Objects;

public class VoiceQcOss extends QcOSS {

    public static final String type = "voice";

    public VoiceQcOss(OSSConfig config) {
//        super(config);
    }

    private static QcOSS oss;

    @Data
    public class OSSConfig {
        private String accessKey;
        private String accessSecret;
    }

//    public OSS(OSSConfig config ) {
//        //todo 从数据库中读取oss的配置
//        // oss = ?
//    }

    public static QcOSS get() {
        if (Objects.isNull(oss)) {
            throw new NullPointerException();
        }
        return oss;
    }

    public String upload(byte[] content) throws OSSException {
        //todo 上传文件并返回访问地址
        return null;
    }
}
