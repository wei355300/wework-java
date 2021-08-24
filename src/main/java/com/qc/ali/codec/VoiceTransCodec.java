package com.qc.ali.codec;

import com.qc.ali.OssConfig;
import com.qc.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class VoiceTransCodec {

    public static final String Content_Type_MP3 = "audio/mp3";

    @Autowired
    public VoiceTransCodec(ConfigService configService) {
        OssConfig config = OssConfig.toOssConfig(configService);
        //todo
    }

    /**
     * 返回转码后的地址
     * @param mediaUrl
     * @param contentType
     * @return
     */
    public String transCodec(String mediaUrl, String contentType) throws CodecFailureException {
        if(!Content_Type_MP3.equals(contentType))
            throw new IllegalArgumentException("UnSupport Content Type: "+ contentType);

        //todo
        return "";//fixme
    }
}
