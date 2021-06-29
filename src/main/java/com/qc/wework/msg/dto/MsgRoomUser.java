package com.qc.wework.msg.dto;

import lombok.Data;

@Data
public class MsgRoomUser {

    private String id;
    private String name;
    private String position;
    private String thumbAvatar;
    /**
     * 1: 公司员工
     * 2: 客户
     */
    private String type;
}
