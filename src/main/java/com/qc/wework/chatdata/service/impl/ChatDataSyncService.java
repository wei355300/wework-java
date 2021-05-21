package com.qc.wework.chatdata.service.impl;

import com.qc.ali.oss.UploaderStrategy;
import com.qc.config.ConfigService;
import com.qc.wework.msg.exception.FinanceException;
import com.qc.wework.chatdata.mapper.ChatDataMapper;
import com.tencent.wework.Finance;
import com.qc.utils.JsonUtils;
import com.qc.wework.chatdata.ChatDataConfig;
import me.chanjar.weixin.cp.api.WxCpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class ChatDataSyncService {

    private static final Logger logger = LoggerFactory.getLogger(ChatDataSyncService.class);

    private interface Cfg {
        String MODULE = "wechat_cp_app";
        String CODE = "chatdata";
    }

    @Autowired
    private ConfigService configService;
    @Autowired
    private ChatDataMapper chatDataMapper;
    @Autowired
    private UploaderStrategy uploaderStrategy;

    private WxCpService wxCpService;
    private MediaDataParser mediaDataParser;
    private ChatDataParser chatDataParser;
    private ChatDataSyncer chatDataSyncer;

    @PostConstruct
    private void init() throws FinanceException {

        String configs = configService.getConfig(Cfg.MODULE, Cfg.CODE);
        ChatDataConfig chatDataConfig = JsonUtils.parser(configs, ChatDataConfig.class);
        wxCpService = chatDataConfig.parse();
        if(chatDataConfig.isEnable() && configService.isEnableChatData()) {
            initFinance();
        }
        chatDataSyncer = new ChatDataSyncer(wxCpService, chatDataMapper);
        chatDataParser = new ChatDataParser(wxCpService, chatDataMapper);
        mediaDataParser = new MediaDataParser(wxCpService, uploaderStrategy, chatDataMapper);
    }


    private void initFinance() throws FinanceException {

        logger.info("os.name: " + System.getProperties().getProperty("os.name"));
        logger.info("java.library.path: " + System.getProperty("java.library.path"));
        logger.info("load library...");

        try {
            if (System.getProperties().getProperty("os.name").contains("Win")) {
                System.loadLibrary("WeWorkFinanceSdk");
            }
            else {
                System.loadLibrary("WeWorkFinanceSdk_Java");
            }
            Finance.isLoadedLib = true;
        }
        catch (Exception | UnsatisfiedLinkError e) {
            Finance.isLoadedLib = false;
            logger.info("WeWorkFinanceSdk failed");
            e.printStackTrace();
            throw new FinanceException("无法启动 Finance");
        }
    }

    @Async
    public void syncAndParse() throws FinanceException {
        prepare();
        /*
         * fixme 可以采用注册机制, 以任务链路的方式调用执行,
         *
         */
        chatDataSyncer.parse();
        chatDataParser.parse();
        mediaDataParser.parse();
    }

    public void syncChatData() throws FinanceException {
        prepare();
        chatDataSyncer.parse();
    }

    public void parseChatData() throws FinanceException {
        prepare();
        chatDataParser.parse();
    }

    public void parseMediaData() throws FinanceException {
        prepare();
        mediaDataParser.parse();
    }

    private void prepare() throws FinanceException {
        if (!Finance.isLoadedLib) {
            throw new FinanceException("拉取任务初始化失败, 请检查");
        }
        if (!AbstractChatDataParser.isDone()) {
            throw new FinanceException("拉取任务仍在执行, 请等待任务结束后再重试");
        }
    }
}
