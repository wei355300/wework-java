package com.qc.wework.contact.dto;

import lombok.Data;

@Data
public class Contact {
    private int id;
    private String uid;
    private String uname;
    private String uposition;
    private String thumbAvatar;
    private String avatar;
    private int utype;
}
