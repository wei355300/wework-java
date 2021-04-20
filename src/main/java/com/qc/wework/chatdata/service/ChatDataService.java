package com.qc.wework.chatdata.service;

import com.qc.msg.exception.FinanceException;

public interface ChatDataService {

    void triggerSyncChatData() throws FinanceException;

//    void fetchDataFromWeWork() throws FinanceException;
//
    void parseChatData() throws FinanceException;
//
    void parseMediaData() throws FinanceException;
}
