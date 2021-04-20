package com.qc.wework.contact.external.dto;

import lombok.Data;

@Data
public class ContactExternal {

    private Integer id;
    private String externalUserId;
    private String name;
    private String avatar;
    private Integer type;
    private Integer gender;
    private String position;
    private String corpName;
    private String corpFullName;
}
