package com.qc.ali.codec;

import com.aliyun.oss.OSS;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.mts.model.v20140618.SubmitJobsRequest;
import com.aliyuncs.mts.model.v20140618.SubmitJobsResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.qc.ali.oss.OssUtils;
import com.qc.config.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;


/**
 *
 */
@Slf4j
@Service
public class VoiceTransCodec {

    public static final String Content_Type_MP3 = "audio/mp3";

//    private static final String SUFFIX_MP3 = ".mp3";

    private MediaTransferConfig mediaTransferConfig;
    private IAcsClient acsClient;
    private OSS ossClient;

    @Autowired
    public VoiceTransCodec(ConfigService configService) {
        this.mediaTransferConfig = MediaTransferConfig.toOssConfig(configService);
        DefaultProfile profile = DefaultProfile.getProfile(mediaTransferConfig.getRegion(), mediaTransferConfig.getAccess_key(), mediaTransferConfig.getAccess_secret());
        acsClient = new DefaultAcsClient(profile);

        ossClient = OssUtils.buildClient(mediaTransferConfig.getEndpoint(), mediaTransferConfig.getAccess_key(), mediaTransferConfig.getAccess_secret());
    }

    /**
     * 判断是否已经转码
     * 从 oss 的 bucket 上查找是否存在 mp3的文件
     *
     * @param mediaUrl : 原 oss 上的播放地址
     */
    public boolean isTrans(String mediaUrl) throws MalformedURLException {
        String filePath = getFilePath(mediaUrl);
        return isTransferred(filePath);
    }

    /**
     * 直播将后缀修改为.mp3
     *
     * (转码功能仅是将 amr 转为 mp3格式, 固直接修改地址的后缀)
     *
     * @param mediaUrl
     */
    public String getPlayAddress(String mediaUrl) {
        return replaceFileNameSuffix(mediaUrl, "mp3");
    }

    /**
     * 返回转码后的地址
     * @param mediaUrl
     * @param contentType
     * @return
     */
    public String transCodec(String mediaUrl, String contentType) throws TransferFailureException, MalformedURLException {
        if(!Content_Type_MP3.equals(contentType))
            throw new IllegalArgumentException("UnSupport Content Type: "+ contentType);

        //提取文件名称
        String ossInputOssFilePath = getFilePath(mediaUrl);

        if (isTransferred(ossInputOssFilePath)) {
            throw new TransferFailureException("already transfered");
        }

        JsonObject input = makeAudioInput(ossInputOssFilePath);

        JsonArray outputs = makeAudioOutput(ossInputOssFilePath, "mp3");

        SubmitJobsRequest request = makeRequest(input, outputs);

        submitJob(request);

        return replaceFileNameSuffix(mediaUrl, "mp3");
    }

    private boolean submitJob(SubmitJobsRequest request) {
        // 发起请求并处理应答或异常
        boolean ret = false;
        SubmitJobsResponse response = null;
        try {
            response = acsClient.getAcsResponse(request);
            log.info("RequestId is:"+response.getRequestId());
            if (response.getJobResultList().get(0).getSuccess()) {
                ret = true;
                log.info("JobId is:" + response.getJobResultList().get(0).getJob().getJobId());
            } else {
                log.info("SubmitJobs Failed code:" + response.getJobResultList().get(0).getCode() +
                        " message:" + response.getJobResultList().get(0).getMessage());
            }
        } catch (ServerException e) {
            e.printStackTrace();
            log.warn(e.getErrMsg());
        } catch (ClientException e) {
            e.printStackTrace();
            log.warn(e.getErrMsg());
        }
        return ret;
    }

    private SubmitJobsRequest makeRequest(JsonObject input, JsonArray outputs) {
        SubmitJobsRequest request = new SubmitJobsRequest();

        request.setInput(input.toString());
        request.setOutputs(outputs.toString());
        request.setOutputBucket(mediaTransferConfig.getBucket_name());

        String location = ossClient.getBucketLocation(mediaTransferConfig.getBucket_name());
        request.setOutputLocation(location);
        request.setPipelineId(mediaTransferConfig.getPipeline_id());
        return request;
    }

    private JsonObject makeAudioInput(String inputFilePath) {
        // Input
        JsonObject input = new JsonObject();
        String location = ossClient.getBucketLocation(mediaTransferConfig.getBucket_name());
        input.addProperty("Location", location);
        input.addProperty("Bucket", mediaTransferConfig.getBucket_name());
        try {
            input.addProperty("Object", URLEncoder.encode(inputFilePath, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("input URL encode failed");
        }
        return input;
    }

    private JsonArray makeAudioOutput(String inputFilePath, String fileSuffix) {
        // 输出文件名称: 替换文件后缀, 转为.mp3
        String outputOSSFilePath = replaceFileNameSuffix(inputFilePath, fileSuffix);
        try {
            outputOSSFilePath = URLEncoder.encode(outputOSSFilePath, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("output URL encode failed");
        }
        JsonObject output = new JsonObject();
        output.addProperty("OutputObject", outputOSSFilePath);
        // Ouput->Container
        JsonObject container = new JsonObject();
        container.addProperty("Format", "mp3");
        output.addProperty("Container", container.toString());

        // Ouput->Audio
        JsonObject audio = new JsonObject();
        audio.addProperty("Codec", "MP3");
        audio.addProperty("Bitrate", "128");
        audio.addProperty("Channels", "2");
        audio.addProperty("Samplerate", "44100");
        output.addProperty("Audio", audio.toString());

        // Ouput->TemplateId
        output.addProperty("TemplateId", mediaTransferConfig.getTemplate_id());
        JsonArray outputs = new JsonArray();
        outputs.add(output);

        return outputs;
    }

    /**
     * 从 url 中提取出文件名称, 包括文件后缀
     */
    private String getFilePath(String mediaUrl) throws MalformedURLException {
        String mediaPath = new URL(mediaUrl).getPath();
        return mediaPath.substring(1);
    }

    private String replaceFileNameSuffix(String fileName, String suffix) {
        return fileName.substring(0, fileName.lastIndexOf(".")).concat(".").concat(suffix);
    }

    /**
     * 从 oss 的 bucket 中判断是否已经转码
     */
    private boolean isTransferred(String mediaPath) {
        String ossObject = replaceFileNameSuffix(mediaPath, "mp3");
        return ossClient.doesObjectExist(mediaTransferConfig.getBucket_name(), ossObject);
    }
}
