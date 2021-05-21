package com.qc.wework.employee.service;

import com.qc.base.PaginationResponse;
import com.qc.wework.employee.dto.Department;
import com.qc.wework.employee.dto.Employee;
import com.qc.wework.employee.dto.EmployeeDetail;
import com.qc.wework.employee.exception.EmployeeSyncException;

import java.util.Collection;

/**
 * 操作联系人
 */
public interface EmployeeService {

    void triggerSyncChatData() throws EmployeeSyncException;

    EmployeeDetail getEmployeeById(int id);

    EmployeeDetail getEmployeeByMobile(String mobile);

    PaginationResponse<Employee> listEmployees(int pageNum, int pageSize);

    Collection<Employee> listEmployeesByLimit(int offset, int limit);

    /**
     * 获取所有的部门列表
     *
     * @return
     */
    Collection<Department> listDepartments();
}
