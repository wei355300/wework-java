package com.qc.wework.chatdata.service.impl;

import com.qc.wework.msg.exception.FinanceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractChatDataSync extends AbstractChatData {

    private static final Logger logger = LoggerFactory.getLogger(AbstractChatDataSync.class);

    AbstractChatDataSync(String corpId, String corpSecret) {
        super(corpId, corpSecret);
    }

    abstract void sync() throws FinanceException;
}
