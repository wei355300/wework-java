package com.qc.wework.msg.dto;

import lombok.Data;

import java.util.Date;

@Data
public class MsgRoomContent {

    private String id;
    private MsgRoomUser sender;
    private Date msgTime;
    private String content;
    private String msgType;
}
