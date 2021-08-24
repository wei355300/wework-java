package com.qc.ali.oss;

import com.qc.ali.OssConfig;
import com.qc.config.ConfigService;
import com.qc.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UploaderStrategy {

    private interface Cfg {
        String MODULE = "oss_media";
        String CODE = "bucket_chatdata";
    }

    private final Map<String, Uploader> uploaderMap = new HashMap<>();

    @Autowired
    public UploaderStrategy(ConfigService configService) {
        OssConfig config = toOssConfig(configService);
        register(config);
    }

    private OssConfig toOssConfig(ConfigService configService) {
        configService.getConfig(Cfg.MODULE, Cfg.CODE);
        String configs = configService.getConfig(Cfg.MODULE, Cfg.CODE);
        OssConfig ossConfig = JsonUtils.parser(configs, OssConfig.class);
        return ossConfig;
    }

    private void register(OssConfig config) {
        Uploader uploader = new Uploader(config);
        uploaderMap.put(Uploader.type, uploader);

        uploader = new AppendUploader(config);
        uploaderMap.put(AppendUploader.type, uploader);
    }

    /**
     * @param type
     * @return
     */
    public Uploader get(String type) {
        return uploaderMap.get(type);
    }
}
