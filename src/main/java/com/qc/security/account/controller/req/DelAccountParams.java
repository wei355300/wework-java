package com.qc.security.account.controller.req;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DelAccountParams {
    @NotNull
    private String mobile;
}
