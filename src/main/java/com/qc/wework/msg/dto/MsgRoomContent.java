package com.qc.wework.msg.dto;

import lombok.Data;

import java.util.Date;

@Data
public class MsgRoomContent {

    private String roomId;
    private String senderId;
    private String senderName;
    private Date   sendTime;
    private String content;
    private String msgType;
}
