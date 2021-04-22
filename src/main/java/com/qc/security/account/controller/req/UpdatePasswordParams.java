package com.qc.security.account.controller.req;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
public class UpdatePasswordParams {

    @NotNull
    private String mobile;
    @NotNull
    @Length(min = 6, max = 32, message = "密码至少6位")
    private String newPass;
    @NotNull
    @Length(min = 6, max = 32,  message = "密码至少6位")
    private String newPassConfirm;
}
