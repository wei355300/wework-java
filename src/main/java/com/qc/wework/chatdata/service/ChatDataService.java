package com.qc.wework.chatdata.service;

import com.qc.wework.msg.exception.FinanceException;

public interface ChatDataService {

    void triggerSyncChatData() throws FinanceException;

    void parseChatData() throws FinanceException;
//
    void parseMediaData() throws FinanceException;
}
