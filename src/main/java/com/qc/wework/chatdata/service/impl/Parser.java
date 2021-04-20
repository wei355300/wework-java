package com.qc.wework.chatdata.service.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.qc.wework.chatdata.dto.ChatDataParsed;
import com.qc.wework.chatdata.fetcher.Msg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Objects;

public class Parser {

    private static final Logger logger = LoggerFactory.getLogger(Parser.class);


    public static ChatDataParsed toChatDataParsed(Integer chatDataId, String msgId, String resultContent) {

        JsonObject jo = JsonParser.parseString(resultContent).getAsJsonObject();

        ChatDataParsed chatDataParsed = new ChatDataParsed();
        chatDataParsed.setHistory_id(chatDataId);
        chatDataParsed.setMsgid(msgId);
        chatDataParsed.setContent(resultContent);

        JsonElement jeMsgType = jo.get(Msg.Prop.MSGTYPE);
        if (Objects.nonNull(jeMsgType)) {
            String msgType = jeMsgType.getAsString();
            chatDataParsed.setMsgtype(msgType);

            if (Msg.Type.TEXT.equals(msgType)) {
                String msg = jo.get(Msg.Type.TEXT).getAsJsonObject().get(Msg.Prop.CONTENT).getAsString();
                chatDataParsed.setMsg(msg);
            }
        }
        else {
            chatDataParsed.setMsgtype(Msg.Type.CHANGE_ENTERPRISE_LOG);
        }
        JsonElement jeAction = jo.get(Msg.Prop.ACTION);
        if (Objects.nonNull(jeAction)) {
            chatDataParsed.setAction(jeAction.getAsString());
        }

        JsonElement jeRoomid = jo.get(Msg.Prop.ROOMID);
        if (Objects.nonNull(jeRoomid)) {
            chatDataParsed.setRoomid(jeRoomid.getAsString());
        }

        JsonElement jeMsgtime = jo.get(Msg.Prop.MSGTIME);
        if (Objects.nonNull(jeMsgtime)) {
            chatDataParsed.setMsgtime(new Date(jeMsgtime.getAsLong()));
        }

        return chatDataParsed;
    }
}
