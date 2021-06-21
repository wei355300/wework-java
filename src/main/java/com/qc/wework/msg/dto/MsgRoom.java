package com.qc.wework.msg.dto;

import lombok.Data;

import java.util.Date;

@Data
public class MsgRoom {

    private String roomId;
    private String name;
    private String avatar;
    private Date lastTime;
    private String lastMsg;
}
