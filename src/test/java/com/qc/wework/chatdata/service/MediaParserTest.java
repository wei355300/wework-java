package com.qc.wework.chatdata.service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.qc.wework.msg.exception.FinanceException;
import com.qc.wework.chatdata.dto.ChatDataParsed;
import com.qc.wework.chatdata.entity.ImageEntity;
import com.qc.wework.chatdata.mapper.ChatDataMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

@ActiveProfiles("dev")
@SpringBootTest
public class MediaParserTest {

    @Autowired
    private ChatDataService chatDataService;

    @Autowired
    private ChatDataMapper chatDataMapper;

    @Test
    public void testSyncMediaData() throws IOException, FinanceException {
        chatDataService.parseMediaData();
    }

    @Test
    public void testJsonParse() throws IOException, FinanceException {
        ChatDataParsed data = chatDataMapper.getChatDataParsedByHistoryId(34985);
        String content = data.getContent();

        JsonObject jo = JsonParser.parseString(content).getAsJsonObject();
        JsonElement jeType = jo.get("image");
        ImageEntity fe = (new Gson()).fromJson(jeType, ImageEntity.class);


        System.out.println(fe);
    }

}
