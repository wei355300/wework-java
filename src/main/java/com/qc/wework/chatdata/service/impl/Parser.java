package com.qc.wework.chatdata.service.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.qc.wework.chatdata.dto.ChatDataParsed;
import com.qc.wework.chatdata.Msg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Parser {

    private static final Logger logger = LoggerFactory.getLogger(Parser.class);

    private static final String EMPTY_STR = "";

    /**
     *
     * @param chatDataId
     * @param msgId
     * @param resultContent
     * @return
     */
    public static ChatDataParsed toChatDataParsed(Integer chatDataId, String msgId, String resultContent) {

        JsonObject jo = JsonParser.parseString(resultContent).getAsJsonObject();

        ChatDataParsed chatDataParsed = new ChatDataParsed();
        chatDataParsed.setHistory_id(chatDataId);
        chatDataParsed.setMsgid(msgId);
        chatDataParsed.setContent(resultContent);

        Optional<String> msgType = parseMsgType(jo);
        if (msgType.isPresent()) {
            String type = msgType.get();
            chatDataParsed.setMsgtype(type);

            if (Msg.Type.TEXT.equals(type)) {
                Optional<String> msg = parseMsgContent(jo);
                chatDataParsed.setMsg(msg.orElse(EMPTY_STR));
            }
        }
        else {
            chatDataParsed.setMsgtype(Msg.Type.CHANGE_ENTERPRISE_LOG);
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
        else if (chatDataParsed.getAction().equals(Msg.Action.SEND)) {
            chatDataParsed.setRoomid(concat(from, toList));
        }

        return chatDataParsed;
    }

    private static Optional<String> parseMsgContent(JsonObject jo) {
        String msg = jo.get(Msg.Type.TEXT).getAsJsonObject().get(Msg.Prop.CONTENT).getAsString();
        return Optional.of(msg);
    }

    private static Optional<String> parseMsgType(JsonObject jo) {
        JsonElement jeMsgType = jo.get(Msg.Prop.MSGTYPE);
        if (Objects.nonNull(jeMsgType)) {
            return Optional.of(jeMsgType.getAsString());
        }
        return Optional.empty();
    }

    private static Optional<String> parseAction(JsonObject jo) {
        JsonElement jeAction = jo.get(Msg.Prop.ACTION);
        if (Objects.nonNull(jeAction)) {
            return Optional.of(jeAction.getAsString());
        }
        return Optional.empty();
    }

    private static Optional<Date> parseMsgTime(JsonObject jo) {
        JsonElement jeMsgtime = jo.get(Msg.Prop.MSGTIME);
        if (Objects.nonNull(jeMsgtime)) {
            return Optional.of(new Date(jeMsgtime.getAsLong()));
        }
        return Optional.empty();
    }

    private static Optional<String> parseFrom(JsonObject jo) {
        JsonElement jeFrom = jo.get(Msg.Prop.FROM);
        if (Objects.nonNull(jeFrom)) {
            return Optional.of(jeFrom.getAsString());
        }
        return Optional.empty();
    }

    private static Optional<List<String>> parseToList(JsonObject jo) {
        JsonElement jeToList = jo.get(Msg.Prop.TOLIST);
        if (Objects.isNull(jeToList)) {
            return Optional.empty();
        }
        JsonArray jeToArray = jeToList.getAsJsonArray();
        List<String> toList = new ArrayList(jeToArray.size());
        jeToArray.forEach(toJe -> toList.add(toJe.getAsString()));
        return Optional.of(toList);
    }

    private static Optional<String> parseRoomId(JsonObject jo) {
        JsonElement jeRoomid = jo.get(Msg.Prop.ROOMID);
        if (Objects.nonNull(jeRoomid)) {
            return Optional.ofNullable(jeRoomid.getAsString());
        }
        return Optional.empty();
    }

    private static String concat(Optional<String> from, Optional<List<String>> toList) {
        toList.get().add(from.get());
        return sort(toList.get());
    }

    private static String sort(List<String> toList) {
        Collections.sort(toList);
        return String.join("_", toList);
    }
}
