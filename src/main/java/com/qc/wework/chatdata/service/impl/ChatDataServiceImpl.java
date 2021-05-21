package com.qc.wework.chatdata.service.impl;

import com.qc.wework.chatdata.service.ChatDataService;
import com.qc.wework.msg.exception.FinanceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatDataServiceImpl implements ChatDataService {

    private static final Logger logger = LoggerFactory.getLogger(ChatDataServiceImpl.class);

    @Autowired
    private ChatDataSyncService dataSyncService;

    @Override
    public void triggerSyncChatData() throws FinanceException {
        dataSyncService.syncAndParse();
    }

    @Override
    public void parseChatData() throws FinanceException {
        dataSyncService.parseChatData();
    }

    @Override
    public void parseMediaData() throws FinanceException {
        dataSyncService.parseMediaData();
    }
}
