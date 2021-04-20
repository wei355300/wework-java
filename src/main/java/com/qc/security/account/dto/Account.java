package com.qc.security.account.dto;

import lombok.Data;


@Data
public class Account {
    private Integer id;
    private String name;
    private String mobile;
    private String avatar;
    private String thumbAvatar;
    private String authority;
    private String token;
    private String password;

}
