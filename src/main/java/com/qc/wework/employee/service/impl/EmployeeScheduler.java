package com.qc.wework.employee.service.impl;

import com.qc.config.ConfigService;
import com.qc.wework.employee.exception.EmployeeSyncException;
import com.qc.wework.employee.service.EmployeeService;
import com.qc.wework.msg.exception.FinanceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class EmployeeScheduler {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeScheduler.class);

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ConfigService configService;

    /**
     * 每5小时执行
     *
     * @throws FinanceException
     */
    @Scheduled(cron = "0 0 0/5 * * ?")
    public void syncContactExternal() throws EmployeeSyncException {
        logger.info("开始员工...");
        if (!configService.isEnableEmployeeSync()) {
            logger.info("未激活同步任务, 跳过任务");
            return;
        }
        employeeService.triggerSyncChatData();
        logger.info("员工同步结束 !");
    }
}
