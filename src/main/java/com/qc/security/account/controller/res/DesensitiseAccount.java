package com.qc.security.account.controller.res;

import lombok.Data;

@Data
public class DesensitiseAccount {
    //mapstruct todo
    private Integer id;
    private String name;
    private String mobile;
    private String avatar;
    private String thumbAvatar;
    private String authority;
    private String token;
    private String password;
}
