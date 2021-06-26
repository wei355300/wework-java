package com.qc.wework.employee.controller;

import com.qc.base.PaginationResponse;
import com.qc.base.R;
import com.qc.wework.employee.dto.Employee;
import com.qc.wework.employee.dto.EmployeeDetail;
import com.qc.wework.employee.exception.EmployeeSyncException;
import com.qc.wework.employee.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;


@RestController
@RequestMapping("/api/qc/wework/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/{id}")
    public R<EmployeeDetail> getEmployeeById(@PathVariable("id") Integer employeeId) {
        EmployeeDetail employee = this.employeeService.getEmployeeById(employeeId);
        return R.suc(employee);
    }

    @GetMapping("/list")
    public R<Collection<Employee>> listEmployee(@RequestParam(name = "current", required = false, defaultValue = "1") int pageNum, @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize) {
        PaginationResponse<Employee> list = employeeService.listEmployees(pageNum, pageSize);
        return R.suc(list);
    }

    @PostMapping("/list/sync")
    public R syncEmployees() throws EmployeeSyncException {
        this.employeeService.triggerSyncChatData();
        return R.suc();
    }

//    @GetMapping("/token")
//    public R<String> getToken() {
//        WxCpService wxCpService = wxCpConfiguration.getCpService(WxCpConfiguration.WxCpAppAgent.Employee);
//        String accessToken = null;
//        try {
//            accessToken = wxCpService.getAccessToken();
//        } catch (WxErrorException e) {
//            e.printStackTrace();
//        }
//        return R.suc(accessToken);
//    }

}