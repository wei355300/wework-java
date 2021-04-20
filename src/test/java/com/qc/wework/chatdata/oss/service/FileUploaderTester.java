package com.qc.wework.chatdata.oss.service;

import com.qc.ali.oss.FileUploader;
import com.qc.ali.oss.OssConfig;
import com.qc.ali.oss.OssStrategy;
import com.qc.config.ConfigService;
import com.qc.utils.JsonUtils;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.mockito.Mockito.when;

@SpringBootTest
public class FileUploaderTester {

//    @Mock
//    private ConfigService configService;
//
//    @Mock
//    private FileUploader fileUploader;
//
//    @InjectMocks
//    private OssStrategy ossStrategy;
//
//    @Test
//    public void testGetStories() throws IOException {
//
//        when(configService.getConfig("oss_media", "bucket_chatdata")).thenReturn("{\n" +
//                "    \"access_key\": \"LTAI4G357MZqeY8wo11zUVXm\",\n" +
//                "    \"access_secret\": \"SudctrwwOXQyH773fOsSPVyhTSL7aQ\",\n" +
//                "    \"bucket_name\": \"wework-chatdata-media\",\n" +
//                "    \"endpoint\": \"oss-cn-shanghai.aliyuncs.com\"\n" +
//                "}");
//        when(fileUploader.getConfig()).thenReturn(buildOssConfig());
//
////        when(ossConfig.getAccess_key()).thenReturn("LTAI4G357MZqeY8wo11zUVXm");
////        when(ossConfig.getAccess_secret()).thenReturn("SudctrwwOXQyH773fOsSPVyhTSL7aQ");
////        when(ossConfig.getEndpoint()).thenReturn("oss-cn-shanghai.aliyuncs.com");
////        when(ossConfig.getBucket_name()).thenReturn("wework-chatdata-media");
//
//        ossStrategy.upload(FileUploader.type, "test_file.txt", "testfile".getBytes());
//
//    }
//
//    private OssConfig buildOssConfig() {
//        return JsonUtils.parser("{\n" +
//                "    \"access_key\": \"LTAI4G357MZqeY8wo11zUVXm\",\n" +
//                "    \"access_secret\": \"SudctrwwOXQyH773fOsSPVyhTSL7aQ\",\n" +
//                "    \"bucket_name\": \"wework-chatdata-media\",\n" +
//                "    \"endpoint\": \"oss-cn-shanghai.aliyuncs.com\"\n" +
//                "}", OssConfig.class);
//    }
}
