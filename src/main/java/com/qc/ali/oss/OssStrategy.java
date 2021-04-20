package com.qc.ali.oss;

import com.qc.config.ConfigService;
import com.qc.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class OssStrategy {

    private interface Cfg {
        String MODULE = "oss_media";
        String CODE = "bucket_chatdata";
    }

    private final Map<String, Uploader> ossExecs = new HashMap<>();

    @Autowired
    public OssStrategy(ConfigService configService) {
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
        FileUploader fileUploader = new FileUploader(config);
        ossExecs.put(FileUploader.type, fileUploader);
    }

//    /**
//     * 上传文件
//     * @param type
//     * @param content
//     * @return
//     */
//    public String upload(String type, String resName, byte[] content) {
//        Uploader uploader = ossExecs.get(type);
//        return uploader.start(resName).upload(content).end();
//    }

//    /**
//     * 追加的模式上传文件
//     * @param type
//     * @param content
//     * @return
//     */
//    public boolean uploadAppend(String type, byte[] content) {
//        //todo
//        return false;
//    }
}
