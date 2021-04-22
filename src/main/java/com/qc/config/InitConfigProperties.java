package com.qc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "init.config")
public class InitConfigProperties {

    private List<String> modules;

    private Boolean chatdata;

    private Boolean externalConcat;

    private Boolean employee;
}
