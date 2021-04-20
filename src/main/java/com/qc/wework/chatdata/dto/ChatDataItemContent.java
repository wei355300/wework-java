package com.qc.wework.chatdata.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ChatDataItemContent {

    private int id;
    private int history_id;
    private String msgid;
    private String action;
    private String from;
    private String tolist;
    private String roomid;
    private Date msgtime;
    private String msgtype;
    private String content;
}
