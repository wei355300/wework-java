package com.qc.ali.oss;

public class FileUploader extends Uploader {

    public static final String type = "file";

    public FileUploader(OssConfig config) {
        super(config);
    }
}
