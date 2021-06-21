package com.qc.wework.chatdata.service.impl;

import com.qc.wework.msg.exception.FinanceException;
import com.tencent.wework.Finance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractChatData {

    static final long LIMIT = 100;
    static final String PROXY = null;
    static final String PASWD = null;
    static final long TIMEOUT  = 120;

    private static final Logger logger = LoggerFactory.getLogger(AbstractChatData.class);

    private static boolean done = true;

    private String corpId;
    private String corpSecret;

    AbstractChatData(String corpId, String corpSecret) {
        this.corpId = corpId;
        this.corpSecret = corpSecret;
    }

    /**
     * 调用Finance, 创建sdk,
     * 必需与 {@link #freeFinanceSdk(long)} 配合使用
     * @return
     * @throws FinanceException
     */
    long geneFinanceSdk() throws FinanceException {
        long financeSdk = Finance.NewSdk();
        logger.debug("new finance sdk value : {}", financeSdk);
        logger.info("Finance Sdk info; corpId: {}, secret: {}", corpId, corpSecret);
        long ret = Finance.Init(financeSdk, corpId, corpSecret);
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
        AbstractChatData.done = done;
    }
}
