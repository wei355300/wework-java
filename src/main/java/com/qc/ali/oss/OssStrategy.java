package com.qc.ali.oss;

import com.qc.ali.OssConfig;
import com.qc.config.ConfigService;
import com.qc.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class OssStrategy {

    private final Map<String, Uploader> ossExecs = new HashMap<>();

    @Autowired
    public OssStrategy(ConfigService configService) {
        OssConfig config = OssConfig.toOssConfig(configService);
        register(config);
    }

    private void register(OssConfig config) {
        FileUploader fileUploader = new FileUploader(config);
        ossExecs.put(FileUploader.type, fileUploader);
    }
}
