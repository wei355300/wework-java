package com.qc.ali.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.Getter;

public class OssClientQc {

    @Getter
    private String endpoint;
    @Getter
    private String accessKeyId;
    @Getter
    private String accessKeySecret;
    @Getter
    private String bucketName;
    @Getter
    private String preUrl;

    @Getter
    private OSS ossClient;

    public OssClientQc(OssConfig ossConfig) {
        endpoint = ossConfig.getEndpoint();
        accessKeyId = ossConfig.getAccess_key();
        accessKeySecret = ossConfig.getAccess_secret();
        bucketName = ossConfig.getBucket_name();
        preUrl = ossConfig.getPre_url();

        ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }
}
