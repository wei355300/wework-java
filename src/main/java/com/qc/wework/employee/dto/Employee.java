package com.qc.wework.employee.dto;

import lombok.Data;

@Data
public class Employee {
    private int id;
    private String userId;
    private String name;
    private String mobile;
    private String avatar;
    private String thumbAvatar;
    private String position;
    private String email;
    private String department;
}
