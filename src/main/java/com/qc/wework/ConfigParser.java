package com.qc.wework;

import lombok.Data;
import lombok.val;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.config.impl.WxCpDefaultConfigImpl;

@Data
public class ConfigParser {

    private String corp_id;
    private int agent_id;
    private String aeskey;
    private String token;
    private String secret;

    public WxCpService parse() {
        val configStorage = new WxCpDefaultConfigImpl();
        configStorage.setCorpId(getCorp_id());
        configStorage.setAgentId(getAgent_id());
        configStorage.setCorpSecret(getSecret());
        configStorage.setToken(getToken());
        configStorage.setAesKey(getAeskey());
        val service = new WxCpServiceImpl();
        service.setWxCpConfigStorage(configStorage);
        return service;
    }
}
