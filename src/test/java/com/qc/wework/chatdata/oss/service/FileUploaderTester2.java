package com.qc.wework.chatdata.oss.service;

import com.qc.ali.oss.Uploader;
import com.qc.ali.oss.UploaderStrategy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class FileUploaderTester2 {

    @Autowired
    private UploaderStrategy uploaderStrategy;

    @Test
    public void testGetStories() throws IOException {

        String url = uploaderStrategy.get(Uploader.type).start("demo/test_file.txt", "").upload("testfile".getBytes()).end();
        System.out.println("-->" + url);
    }
}
