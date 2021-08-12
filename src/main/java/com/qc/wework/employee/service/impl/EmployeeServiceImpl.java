package com.qc.wework.employee.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qc.base.PaginationResponse;
import com.qc.wework.employee.dto.Department;
import com.qc.wework.employee.dto.Employee;
import com.qc.wework.employee.dto.EmployeeDetail;
import com.qc.wework.employee.exception.EmployeeSyncException;
import com.qc.wework.employee.mapper.EmployeeMapper;
import com.qc.wework.employee.service.EmployeeService;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private EmployeeSyncService employeeSyncService;

    @Override
    public void triggerSyncChatData() throws EmployeeSyncException {
        try {
            employeeSyncService.syncEmployees();
        } catch (WxErrorException e) {
            throw new EmployeeSyncException();
        }
    }

    @Override
    public EmployeeDetail getEmployeeById(int id) {
        EmployeeDetail employee = employeeMapper.queryById(id);
        return employee;
    }

    @Override
    public EmployeeDetail getEmployeeByMobile(String mobile) {
        EmployeeDetail employee = employeeMapper.queryByMobile(mobile);
        return employee;
    }

    @Override
    public PaginationResponse<Employee> listEmployees(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Employee> list = employeeMapper.list();
        PageInfo page = new PageInfo(list);
        return PaginationResponse.toPagination(page);
    }

    @Override
    public Collection<Employee> listEmployeesByLimit(int offset, int limit) {
        return employeeMapper.queryAllByLimit(offset, limit);
    }

    @Override
    public Collection<Department> listDepartments() {
        return null;
    }
}
