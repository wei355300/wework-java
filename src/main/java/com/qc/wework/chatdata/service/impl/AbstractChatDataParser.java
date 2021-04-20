package com.qc.wework.chatdata.service.impl;

import com.qc.msg.exception.FinanceException;
import com.tencent.wework.Finance;
import me.chanjar.weixin.cp.api.WxCpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractChatDataParser {

    static final long LIMIT = 100;
    static final String PROXY = null;
    static final String PASWD = null;
    static final long TIMEOUT  = 120;

    private static final Logger logger = LoggerFactory.getLogger(AbstractChatDataParser.class);
    private WxCpService wxCpService;

    private static boolean done = true;

    AbstractChatDataParser(WxCpService wxCpService) {
        this.wxCpService = wxCpService;
    }

    abstract void parse() throws FinanceException;

    /**
     * 调用Finance, 创建sdk,
     * 必需与 {@link #freeFinanceSdk(long)} 配合使用
     * @return
     * @throws FinanceException
     */
    long geneFinanceSdk() throws FinanceException {
        long financeSdk = Finance.NewSdk();
        logger.debug("new finance sdk value : {}", financeSdk);
        logger.info("Finance Sdk info; corpId: {}, secret: {}",wxCpService.getWxCpConfigStorage().getCorpId(), wxCpService.getWxCpConfigStorage().getCorpSecret());
        long ret = Finance.Init(financeSdk, wxCpService.getWxCpConfigStorage().getCorpId(), wxCpService.getWxCpConfigStorage().getCorpSecret());
        if (ret != 0) {
            Finance.DestroySdk(financeSdk);
            System.out.println("init sdk err ret " + ret);
            throw new FinanceException("会话存档功能初始化失败! 请稍后重试");
        }
        return financeSdk;
    }

    /**
     * 释放掉finance sdk,
     * 必需与 {@link #geneFinanceSdk()} 配合使用
     * @param sdk
     */
    void freeFinanceSdk(long sdk) {
        Finance.DestroySdk(sdk);
    }

    public static boolean isDone() {
        return done;
    }

    static void setDone(boolean done) {
        AbstractChatDataParser.done = done;
    }
}
