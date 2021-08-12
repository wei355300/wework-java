package com.qc.wework.contact.external.service;

import com.qc.config.ConfigService;
import com.qc.wework.msg.exception.FinanceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ContactExternalScheduler {

    private static final Logger logger = LoggerFactory.getLogger(ContactExternalScheduler.class);

    @Autowired
    private ContactExternalSyncService contactExternalSyncService;

    @Autowired
    private ConfigService configService;

    /**
     * 每5小时执行
     * @throws FinanceException
     */
    @Scheduled(cron = "0 0 0/5 * * ?")
    public void syncContactExternal() {
        logger.info("开始同步外部联系人...");
        if (!configService.isEnableExternalContactSync()) {
            logger.info("未激活同步任务, 跳过任务");
            return;
        }
        contactExternalSyncService.syncExternalContacts();
        logger.info("外部联系人同步结束 !");
    }
}
