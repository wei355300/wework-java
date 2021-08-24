package com.qc.ali.oss;

import com.qc.ali.OssConfig;

public class FileUploader extends Uploader {

    public static final String type = "file";

    public FileUploader(OssConfig config) {
        super(config);
    }
}
