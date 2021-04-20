package com.qc.wework.employee.dto;

import lombok.Data;

@Data
public class Department {

    private Long id;
    private String name;
    private Long parentId;
    private Long order;
}
