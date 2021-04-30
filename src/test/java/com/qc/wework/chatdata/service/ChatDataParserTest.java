package com.qc.wework.chatdata.service;

import com.qc.msg.exception.FinanceException;
import com.qc.wework.chatdata.dto.ChatDataParsed;
import com.qc.wework.chatdata.mapper.ChatDataMapper;
import com.qc.wework.chatdata.service.impl.Parser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
@ActiveProfiles("dev")
public class ChatDataParserTest {

    @Autowired
    private ChatDataMapper chatDataMapper;

    @Autowired
    private ChatDataService chatDataService;

    @Test
    public void testSyncMediaData() throws IOException, FinanceException {
        chatDataService.parseChatData();
    }

    @Test
    public void testParseChatDataContent() {
        int historyId = 101655;
        String msgId = "4921223135490890266_1617849306_external";
        ChatDataParsed chatDataParsed = chatDataMapper.getChatDataParsedByHistoryId(historyId);

        ChatDataParsed ret = Parser.toChatDataParsed(historyId, msgId, chatDataParsed.getContent());

        assertThat(ret.getMsgid(), equalTo(msgId));
    }
}
