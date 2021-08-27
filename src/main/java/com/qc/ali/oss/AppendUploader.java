package com.qc.ali.oss;

import com.aliyun.oss.model.AppendObjectRequest;
import com.aliyun.oss.model.AppendObjectResult;

import java.io.ByteArrayInputStream;
import java.util.Objects;

/**
 * 分片上传
 */
public class AppendUploader extends Uploader {

    public static final String type = "append";

    private AppendObjectRequest appendObjectRequest;
    private AppendObjectResult appendObjectResult;

    public AppendUploader(OssConfig config) {
        super(config);
    }

    @Override
    public Uploader upload(byte[] content) {
        //首次追加
        if (Objects.isNull(appendObjectRequest) || Objects.isNull(appendObjectResult)) {
            appendObjectRequest = new AppendObjectRequest(getOssConfig().getBucket_name(), getResName(), new ByteArrayInputStream(content), getMeta());
            appendObjectRequest.setPosition(0L);
            appendObjectResult = getOssClient().appendObject(appendObjectRequest);
        }
        //后面追加
        else {
            appendObjectRequest.setPosition(appendObjectResult.getNextPosition());
            appendObjectRequest.setInputStream(new ByteArrayInputStream(content));
            appendObjectResult = getOssClient().appendObject(appendObjectRequest);
        }
        return this;
    }

    @Override
    public String end() {
        String url = super.end();
        appendObjectRequest = null;
        appendObjectResult = null;
        return url;
    }
}
