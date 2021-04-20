package com.qc.ali.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import lombok.Getter;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.http.client.utils.DateUtils;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

/**
 * 简单上传
 */
public class Uploader {

    public static final String type = "single";

    @Getter
    private OssConfig config;

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
    @Getter
    private String resName;
//    @Getter
//    private String contentType;
    @Getter
    private ObjectMetadata meta;

    public Uploader(OssConfig config) {
        this.config = config;
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        endpoint = config.getEndpoint();//"http://oss-cn-hangzhou.aliyuncs.com";
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        accessKeyId = config.getAccess_key();// "<yourAccessKeyId>";
        accessKeySecret = config.getAccess_secret();//"<yourAccessKeySecret>";
        bucketName = config.getBucket_name();
        preUrl = config.getPre_url();
    }

    /**
     * 准备上传文件
     * @param resName resName 资源名称, 可以按 '/' 分路径存放
     * @return
     */
    public Uploader start(String resName, String contentType) {
//        this.resName = resName;
        this.resName = DateUtils.formatDate(new Date(), "yyyy-MM-dd").concat("/").concat(resName);
//        this.contentType = contentType;
        // 创建OSSClient实例。
        ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
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
//        OssConfig config = getConfig();
//        // Endpoint以杭州为例，其它Region请按实际情况填写。
//        String endpoint = config.getEndpoint();//"http://oss-cn-hangzhou.aliyuncs.com";
//        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
//        String accessKeyId = config.getAccess_key();// "<yourAccessKeyId>";
//        String accessKeySecret = config.getAccess_secret();//"<yourAccessKeySecret>";
//
//        // 创建OSSClient实例。
//        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 创建PutObjectRequest对象。
        //        String content = "Hello OSS";
        // <yourObjectName>表示上传文件到OSS时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg。
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, resName, new ByteArrayInputStream(content));

        // 如果需要上传时设置存储类型与访问权限，请参考以下示例代码。
//         ObjectMetadata metadata = new ObjectMetadata();
//         metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
//         metadata.setObjectAcl(CannedAccessControlList.Private);
//        meta.setContentType(contentType);
        putObjectRequest.setMetadata(meta);

        // 上传字符串。
        ossClient.putObject(putObjectRequest);
        return this;

    }

    public String end() {
        String url = preUrl.concat("/").concat(resName);

        // 关闭OSSClient。
        ossClient.shutdown();
        resName = null;
//        contentType = null;
        // 创建OSSClient实例。
        ossClient = null;
        meta = null;

        return url;
    }
}
