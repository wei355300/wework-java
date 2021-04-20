package com.qc.wework.chatdata.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ChatDataItem {

    private int id;
    private long seq;
    private String msgid;
    private String publickey_ver;
    private String encrypt_random_key;
    private String encrypt_chat_msg;
    private Date creationDate;
}
