package com.qc.wework.chatdata.service;

import com.qc.msg.exception.FinanceException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class ChatDataParserTest {

    @Autowired
    private ChatDataService chatDataService;

    @Test
    public void testSyncMediaData() throws IOException, FinanceException {
        chatDataService.parseChatData();
    }
}
