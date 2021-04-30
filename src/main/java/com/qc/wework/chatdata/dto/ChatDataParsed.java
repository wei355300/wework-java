package com.qc.wework.chatdata.dto;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@ToString
@Data
public class ChatDataParsed {
    private int id;
    private int history_id;
    private String msgid;
    private String action;
    private String msgtype;
    private String roomid;
    private Date msgtime;
    private String msg;
    private String content;
    private List<String> toList;
    private String from;
}
