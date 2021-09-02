package com.qc.ali.codec;

import com.qc.ali.oss.OssConfig;
import com.qc.config.ConfigService;
import com.qc.utils.JsonUtils;
import lombok.Data;

/**
 * oss 配置 + mts 配置 的合集
 */
@Data
public class MediaTransferConfig extends OssConfig {

    private String pipeline_id;
    private String template_id;
    private String region;

    private interface Cfg {
        String MODULE = "oss_media";
        String CODE = "mts";
    }

    public static MediaTransferConfig toOssConfig(ConfigService configService) {
        configService.getConfig(Cfg.MODULE, Cfg.CODE);
        String configs = configService.getConfig(Cfg.MODULE, Cfg.CODE);
        MediaTransferConfig mediaTransferConfig = JsonUtils.parser(configs, MediaTransferConfig.class);
        return mediaTransferConfig;
    }
}
