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

//    /**
//     * 准备上传文件
//     * @param resName resName 资源名称, 可以按 '/' 分路径存放
//     * @return
//     */
//    @Override
//    public Uploader start(String resName) {
//        super.start(resName);
//
//        AppendObjectRequest appendObjectRequest
////        this.resName = resName;
////        // 创建OSSClient实例。
////        ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
////        return this;
//    }

    @Override
    public Uploader upload(byte[] content) {
//        OssConfig config = getConfig();
//        // Endpoint以杭州为例，其它Region请按实际情况填写。
//        String endpoint = config.getEndpoint();//"https://oss-cn-hangzhou.aliyuncs.com";
//// 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
//        String accessKeyId = config.getAccess_key();// "<yourAccessKeyId>";
//        String accessKeySecret = config.getAccess_secret();//"<yourAccessKeySecret>";

//        String content1 = "Hello OSS A \n";
//        String content2 = "Hello OSS B \n";
//        String content3 = "Hello OSS C \n";

// 创建OSSClient实例。
//        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

//        ObjectMetadata meta = new ObjectMetadata();
// 指定上传的内容类型。
//        meta.setContentType("text/plain");

// 通过AppendObjectRequest设置多个参数。
        //首次追加
        if (Objects.isNull(appendObjectRequest) || Objects.isNull(appendObjectResult)) {
            appendObjectRequest = new AppendObjectRequest(getBucketName(), getResName(), new ByteArrayInputStream(content), getMeta());
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
//        AppendObjectRequest appendObjectRequest = new AppendObjectRequest("<yourBucketName>", "<yourObjectName>", new ByteArrayInputStream(content1.getBytes()), getMeta());

// 通过AppendObjectRequest设置单个参数。
// 设置Bucket名称。
//appendObjectRequest.setBucketName("<yourBucketName>");
// 设置Object名称。即不包含Bucket名称在内的Object的完整路径，例如example/test.txt。
//appendObjectRequest.setKey("<yourObjectName>");
// 设置待追加的内容。有两种可选类型：InputStream类型和File类型。这里为InputStream类型。
//appendObjectRequest.setInputStream(new ByteArrayInputStream(content1.getBytes()));
// 设置待追加的内容。有两种可选类型：InputStream类型和File类型。这里为File类型。
//appendObjectRequest.setFile(new File("<yourLocalFile>"));
// 指定文件的元信息，第一次追加时有效。
//appendObjectRequest.setMetadata(meta);

// 第一次追加。
// 设置文件的追加位置。
//        AppendObjectResult appendObjectResult = getOssClient().appendObject(appendObjectRequest);
// 文件的64位CRC值。此值根据ECMA-182标准计算得出。
//        System.out.println(appendObjectResult.getObjectCRC());

//// 第二次追加。
//// nextPosition指明下一次请求中应当提供的Position，即文件当前的长度。
//        appendObjectRequest.setPosition(appendObjectResult.getNextPosition());
//        appendObjectRequest.setInputStream(new ByteArrayInputStream(content2.getBytes()));
//        appendObjectResult = getOssClient().appendObject(appendObjectRequest);
//
//// 第三次追加。
//        appendObjectRequest.setPosition(appendObjectResult.getNextPosition());
//        appendObjectRequest.setInputStream(new ByteArrayInputStream(content3.getBytes()));
//        appendObjectResult = getOssClient().appendObject(appendObjectRequest);

// 关闭OSSClient。
//        ossClient.shutdown();
//
//        return config.getPre_url().concat("/").concat(resName);
    }

    @Override
    public String end() {
        String url = super.end();
        appendObjectRequest = null;
        appendObjectResult = null;
        return url;
    }
}
