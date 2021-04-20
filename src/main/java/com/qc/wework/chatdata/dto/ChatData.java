package com.qc.wework.chatdata.dto;

import lombok.Data;

import java.util.List;

@Data
public class ChatData {

    private Integer id;
    private Integer errCode;
    private String errMsg;
    private List<ChatDataItem> chatdata;

}
