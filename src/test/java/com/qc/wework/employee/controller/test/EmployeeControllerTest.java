package com.qc.wework.employee.controller.test;

import com.qc.wework.employee.service.EmployeeService;
import me.chanjar.weixin.common.error.WxErrorException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmployeeControllerTest {

    @Autowired
    private EmployeeService employeeService;

    @Test
    public void testSyncEmployeeDetail() throws WxErrorException {
        employeeService.syncEmployees();
    }
}
