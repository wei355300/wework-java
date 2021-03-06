package com.qc.config;

import com.qc.config.dto.Config;
import com.qc.config.mapper.ConfigMapper;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
@Configuration
@EnableConfigurationProperties(InitConfigProperties.class)
public class ConfigService {

    private static Logger logger = LoggerFactory.getLogger(ConfigService.class);

    @Autowired
    private InitConfigProperties initConfigProperties;

    @Autowired
    private ConfigMapper configMapper;

    private Map<String, String> configMap = new HashMap<>();

    @PostConstruct
    public void initServices() {
        logger.info("init ConfigService to loading module configs");
        List<String> initModules = initConfigProperties.getModules();
        Collection<Config> configs = configMapper.queryByModules(initModules);
        toModuleConfigs(configs);
    }

    private void toModuleConfigs(Collection<Config> configs) {
        configs.forEach(c -> {
            configMap.put(mAc(c.getModule(), c.getCode()), c.getValue());
        });
    }

    private String mAc(String module, String code) {
        return module.concat("-").concat(code);
    }

    public String getConfig(String module, String code) {
        if (Objects.isNull(module) || Objects.isNull(code)) {
            throw new NullPointerException();
        }
        String v = configMap.get(mAc(module, code));
        if (Objects.isNull(v)) {
            throw new NullPointerException();
        }
        return v;
    }

    public boolean isEnableChatData() {
        return initConfigProperties.getChatdata();
    }

    public boolean isEnableEmployeeSync() {
        return initConfigProperties.getEmployee();
    }

    public boolean isEnableExternalContactSync() {
        return initConfigProperties.getExternalContact();
    }
}
