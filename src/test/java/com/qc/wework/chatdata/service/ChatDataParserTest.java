package com.qc.wework.chatdata.service;

import com.google.common.collect.Lists;
import com.qc.msg.exception.FinanceException;
import com.qc.wework.chatdata.dto.ChatDataItem;
import com.qc.wework.chatdata.dto.ChatDataParsed;
import com.qc.wework.chatdata.mapper.ChatDataMapper;
import com.qc.wework.chatdata.service.impl.Parser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
@ActiveProfiles("dev")
public class ChatDataParserTest {

    @Autowired
    private ChatDataMapper chatDataMapper;

    @Autowired
    private ChatDataService chatDataService;

    private static int time = 0;

    @Test
    public void testSyncMediaData() throws IOException, FinanceException {
        chatDataService.parseChatData();
    }

    @Test
    public void testParseChatDataContent() {
        int historyId = 14983;
//        String msgId = "4921223135490890266_1617849306_external";
        ChatDataParsed chatDataParsed = chatDataMapper.getChatDataParsedByHistoryId(historyId);

        ChatDataParsed ret = Parser.getInstance().toChatDataParsed(historyId, chatDataParsed.getMsgid(), chatDataParsed.getContent());

        List r = Lists.newArrayList(ret);

//        chatDataMapper.insertChatDataParsed(r);
        chatDataMapper.insertChatDataRoomShip(r);

//        assertThat(ret.getMsgid(), equalTo(msgId));
    }

    @Test
    public void testParseChatDataRoomShip() {
        loop(100, 1189);
    }

    private void loop(int limit, int offset) {
        time++;
        List<ChatDataParsed> list = chatDataMapper.getParseChatDataItem(limit, offset);
        p(list);
//        if (list.size() == limit && time < 2) {
        if (list.size() == limit) {
            loop(limit, offset + limit);
        }
    }

    private void p(List<ChatDataParsed> list) {
        List r = Lists.newArrayList();
        list.forEach(c -> {
            ChatDataParsed ret = Parser.getInstance().toChatDataParsed(c.getHistory_id(), c.getMsgid(), c.getContent());


            r.add(ret);
//        chatDataMapper.insertChatDataParsed(r);

//            assertThat(ret.getMsgid(), equalTo(msgId));
        });
        chatDataMapper.insertChatDataRoomShip(r);
    }


}
