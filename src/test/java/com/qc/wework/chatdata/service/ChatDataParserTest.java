package com.qc.wework.chatdata.service;

import com.google.common.collect.Lists;
import com.qc.wework.chatdata.ChatDataMsg;
import com.qc.wework.msg.exception.FinanceException;
import com.qc.wework.chatdata.dto.ChatDataParsed;
import com.qc.wework.chatdata.mapper.ChatDataMapper;
import com.qc.wework.chatdata.service.impl.Parser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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
        chatDataMapper.insertChatDataRoomUser(r);

//        assertThat(ret.getMsgid(), equalTo(msgId));
    }

    @Test
    public void testParseChatDataRoomUser() {
        parseChatRoomUser();
//        int historyId = getHistoryIdFromLastedParsedRoomUser();
//        System.out.println("historyId:"+historyId);
//        loopSaveRoomUser(historyId, 30);

//        loop(10, 0);
//        {
//            int historyId = 13799;
//            ChatDataParsed chatDataParsed = chatDataMapper.getChatDataParsedByHistoryId(historyId);
//
//            ChatDataParsed ret = Parser.getInstance().toChatDataParsed(historyId, chatDataParsed.getMsgid(), chatDataParsed.getContent());
//
//            List<ChatDataParsed> r = Lists.newArrayList(ret);
//            List<ChatDataParsed> roomShipList = r.stream().filter(c -> !ChatDataMsg.Action.SWITCH.equals(c.getAction())).collect(Collectors.toList());
//            chatDataMapper.insertChatDataRoomUser(roomShipList);
//        }
    }

    @Transactional
    @Rollback(false)
    public void parseChatRoomUser() {
        loopSaveRoomUser();
    }

    private void loopSaveRoomUser() {
        time++;
        int historyId = getHistoryIdFromLastedParsedRoomUser();
        int limit = 30;
        System.out.println("historyId:"+historyId);
        List<ChatDataParsed> list = chatDataMapper.getParsedChatDataItem(historyId, limit);
        saveRoomUser(list);

        if (limit == list.size()) {
            testParseChatDataRoomUser();
        }
    }

    private void saveRoomUser(List<ChatDataParsed> list) {
        List<ChatDataParsed> r = Lists.newArrayList();
        list.forEach(c -> {
            ChatDataParsed ret = Parser.getInstance().toChatDataParsed(c.getHistory_id(), c.getMsgid(), c.getContent());

            r.add(ret);

            System.out.println("roomid:..." + ret.getRoomid() + "...");

        });
        List<ChatDataParsed> roomShipList = r.stream().filter(c -> !ChatDataMsg.Action.SWITCH.equals(c.getAction())).collect(Collectors.toList());
        chatDataMapper.insertChatDataRoomUser(roomShipList);
    }

    private int getHistoryIdFromLastedParsedRoomUser() {
        return chatDataMapper.getHistoryIdOFLastedParsedRoomUser();
    }


}
