package com.qc.wework.chatdata.service.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.qc.wework.chatdata.dto.ChatDataParsed;
import com.qc.wework.chatdata.ChatDataMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Parser {

    private final Logger logger = LoggerFactory.getLogger(Parser.class);

    private final String EMPTY_STR = "";

    private static Parser ins = new Parser();

    private Parser() {
        ins = this;
    }

    public static Parser getInstance() {
        return ins;
    }

    /**
     *
     * @param chatDataId
     * @param msgId
     * @param resultContent
     * @return
     */
    public ChatDataParsed toChatDataParsed(Integer chatDataId, String msgId, String resultContent) {

        logger.debug("解析内容", resultContent);

        JsonObject jo = JsonParser.parseString(resultContent).getAsJsonObject();

        ChatDataParsed chatDataParsed = new ChatDataParsed();
        chatDataParsed.setHistory_id(chatDataId);
        chatDataParsed.setMsgid(msgId);
        chatDataParsed.setContent(resultContent);

        Optional<String> msgType = parseMsgType(jo);
        if (msgType.isPresent()) {
            String type = msgType.get();
            chatDataParsed.setMsgtype(type);

            if (ChatDataMsg.Type.TEXT.equals(type)) {
                Optional<String> msg = parseMsgContent(jo);
                chatDataParsed.setMsg(msg.orElse(EMPTY_STR));
            }
        }
        else {
            chatDataParsed.setMsgtype(ChatDataMsg.Type.CHANGE_ENTERPRISE_LOG);
        }


        Optional<String> action = parseAction(jo);
        chatDataParsed.setAction(action.orElse(EMPTY_STR));

        Optional<Date> msgTime = parseMsgTime(jo);
        chatDataParsed.setMsgtime(msgTime.orElse(new Date()));

        Optional<String> from = parseFrom(jo);
        chatDataParsed.setFrom(from.orElse(EMPTY_STR));

        Optional<List<String>> toList = parseToList(jo);
        chatDataParsed.setToList(toList.orElse(Collections.EMPTY_LIST));

        //roomid
        //存在, 表示是个会话聊天
        //不存在, 将 from与to合并(from+to排序), 默认变为群聊
        Optional<String> roomId = parseRoomId(jo);
        if (roomId.isPresent()) {
            chatDataParsed.setRoomid(roomId.orElse(EMPTY_STR));
        }
        else if (chatDataParsed.getAction().equals(ChatDataMsg.Action.SEND)) {
            chatDataParsed.setRoomid(concat(from, toList));
        }

        logger.debug("解析结果", chatDataParsed.toString());

        return chatDataParsed;
    }

    private void parseRoom() {

    }

    private Optional<String> parseMsgContent(JsonObject jo) {
        String msg = jo.get(ChatDataMsg.Type.TEXT).getAsJsonObject().get(ChatDataMsg.Prop.CONTENT).getAsString();
        return Optional.of(msg);
    }

    private Optional<String> parseMsgType(JsonObject jo) {
        JsonElement jeMsgType = jo.get(ChatDataMsg.Prop.MSGTYPE);
        if (Objects.nonNull(jeMsgType)) {
            return Optional.of(jeMsgType.getAsString());
        }
        return Optional.empty();
    }

    private Optional<String> parseAction(JsonObject jo) {
        JsonElement jeAction = jo.get(ChatDataMsg.Prop.ACTION);
        if (Objects.nonNull(jeAction)) {
            return Optional.of(jeAction.getAsString());
        }
        return Optional.empty();
    }

    private Optional<Date> parseMsgTime(JsonObject jo) {
        JsonElement jeMsgtime = jo.get(ChatDataMsg.Prop.MSGTIME);
        if (Objects.nonNull(jeMsgtime)) {
            return Optional.of(new Date(jeMsgtime.getAsLong()));
        }
        return Optional.empty();
    }

    private Optional<String> parseFrom(JsonObject jo) {
        JsonElement jeFrom = jo.get(ChatDataMsg.Prop.FROM);
        if (Objects.nonNull(jeFrom)) {
            return Optional.of(jeFrom.getAsString());
        }
        return Optional.empty();
    }

    private Optional<List<String>> parseToList(JsonObject jo) {
        JsonElement jeToList = jo.get(ChatDataMsg.Prop.TOLIST);
        if (Objects.isNull(jeToList)) {
            return Optional.empty();
        }
        JsonArray jeToArray = jeToList.getAsJsonArray();
        List<String> toList = new ArrayList(jeToArray.size());
        jeToArray.forEach(toJe -> toList.add(toJe.getAsString()));
        return Optional.of(toList);
    }

    private Optional<String> parseRoomId(JsonObject jo) {
        JsonElement jeRoomid = jo.get(ChatDataMsg.Prop.ROOMID);
        if (Objects.nonNull(jeRoomid)) {
            return Optional.ofNullable(jeRoomid.getAsString());
        }
        return Optional.empty();
    }

    private String concat(Optional<String> from, Optional<List<String>> toList) {
        toList.get().add(from.get());
        return sort(toList.get());
    }

    private String sort(List<String> toList) {
        Collections.sort(toList);
        String str = String.join("_", toList);
        return str.length() > 100 ? str.substring(0, 100) : str;
    }
}
