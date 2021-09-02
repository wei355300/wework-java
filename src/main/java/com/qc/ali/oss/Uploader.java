package com.qc.ali.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import lombok.Getter;
import org.apache.http.client.utils.DateUtils;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.Objects;

/**
 * 简单上传
 */
public class Uploader {

    public static final String type = "single";

    @Getter
    private OSS ossClient;
    @Getter
    private OssConfig ossConfig;
    @Getter
    private String resName;
    @Getter
    private ObjectMetadata meta;

    public Uploader(OssConfig ossConfig) {
        this.ossConfig = ossConfig;
    }

    /**
     * 准备上传文件
     * @param resName resName 资源名称, 可以按 '/' 分路径存放
     * @return
     */
    public Uploader start(String resName, String contentType) {
        this.resName = DateUtils.formatDate(new Date(), "yyyy-MM-dd").concat("/").concat(resName);
        ossClient = OssUtils.buildClient(ossConfig.getEndpoint(), ossConfig.getAccess_key(), ossConfig.getAccess_secret());
        meta = new ObjectMetadata();
        if(Objects.nonNull(contentType)) {
            meta.setContentType(contentType);
        }
        return this;
    }

    /**
     * 开始上传文件
     * @param content 文件内容
     */
    public Uploader upload(byte[] content) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(ossConfig.getBucket_name(), resName, new ByteArrayInputStream(content));
        putObjectRequest.setMetadata(meta);
        ossClient.putObject(putObjectRequest);
        return this;
    }

    public String end() {
        String url = ossConfig.getPre_url().concat("/").concat(resName);
        ossClient.shutdown();
        resName = null;
        ossClient = null;
        meta = null;
        return url;
    }
}
