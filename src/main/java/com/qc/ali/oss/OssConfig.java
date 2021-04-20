package com.qc.ali.oss;

import lombok.Data;

@Data
public class OssConfig {

    private String endpoint;
    private String access_key;
    private String access_secret;
    private String bucket_name;
    private String pre_url;
}
