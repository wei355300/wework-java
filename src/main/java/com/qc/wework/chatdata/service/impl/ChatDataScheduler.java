package com.qc.wework.chatdata.service.impl;

import com.qc.msg.exception.FinanceException;
import com.qc.wework.chatdata.service.ChatDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ChatDataScheduler {

    private static final Logger logger = LoggerFactory.getLogger(ChatDataScheduler.class);

    @Autowired
    private ChatDataService chatDataService;

    /**
     * 每3小时执行
     * @throws FinanceException
     */
    @Scheduled(cron = "0 0 0/3 * * ?")
    public void syncMsg() throws FinanceException {
        logger.info("开始同步会话存档内容...");
        chatDataService.triggerSyncChatData();
        logger.info("同步结束 !");
    }
}