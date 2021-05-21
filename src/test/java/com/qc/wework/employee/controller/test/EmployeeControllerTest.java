package com.qc.wework.employee.controller.test;

import com.qc.wework.employee.exception.EmployeeSyncException;
import com.qc.wework.employee.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("dev")
@SpringBootTest
public class EmployeeControllerTest {

    @Autowired
    private EmployeeService employeeService;

    @Test
    public void testSyncEmployeeDetail() throws EmployeeSyncException {
        employeeService.triggerSyncChatData();
    }
}
