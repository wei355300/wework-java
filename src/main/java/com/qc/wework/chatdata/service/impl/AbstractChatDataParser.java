package com.qc.wework.chatdata.service.impl;

import com.qc.wework.msg.exception.FinanceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractChatDataParser extends AbstractChatData {

    private static final Logger logger = LoggerFactory.getLogger(AbstractChatDataParser.class);

    AbstractChatDataParser(String corpId, String corpSecret) {
        super(corpId, corpSecret);
    }

    abstract void parse() throws FinanceException;
}
