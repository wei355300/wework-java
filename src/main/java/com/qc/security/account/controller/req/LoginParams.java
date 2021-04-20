package com.qc.security.account.controller.req;

import lombok.Data;

@Data
public class LoginParams {

    private String mobile;
    private String password;
}
