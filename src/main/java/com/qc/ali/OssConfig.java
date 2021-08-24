package com.qc.ali;

import com.qc.config.ConfigService;
import com.qc.utils.JsonUtils;
import lombok.Data;

@Data
public class OssConfig {

    private String endpoint;
    private String access_key;
    private String access_secret;
    private String bucket_name;
    private String pre_url;

    private interface Cfg {
        String MODULE = "oss_media";
        String CODE = "bucket_chatdata";
    }

    public static OssConfig toOssConfig(ConfigService configService) {
        configService.getConfig(Cfg.MODULE, Cfg.CODE);
        String configs = configService.getConfig(Cfg.MODULE, Cfg.CODE);
        OssConfig ossConfig = JsonUtils.parser(configs, OssConfig.class);
        return ossConfig;
    }
}
