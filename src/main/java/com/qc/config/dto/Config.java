package com.qc.config.dto;

import com.qc.utils.JsonUtils;
import lombok.Data;

@Data
public class Config {

    private Integer id;
    private String module;
    private String code;
    private String value;

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}
