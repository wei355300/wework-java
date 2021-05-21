package com.qc.wework.employee.service.impl;

import com.qc.base.QcBaseException;
import com.qc.config.ConfigService;
import com.qc.utils.JsonUtils;
import com.qc.wework.contact.ContactConfig;
import com.qc.wework.employee.mapper.EmployeeMapper;
import lombok.Getter;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.WxCpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeSyncService {

    private interface Cfg {
        String MODULE = "wechat_cp_app";
        String CODE = "user";
    }

    @Autowired
    private EmployeeMapper employeeMapper;

    @Getter
    private WxCpService wxCpService;

    private EmployeeSyncService(@Autowired ConfigService configService) {
        toWxCpService(configService);
    }

    private void toWxCpService(ConfigService configService) {
        String configs = configService.getConfig(Cfg.MODULE, Cfg.CODE);
        ContactConfig contactConfig = JsonUtils.parser(configs, ContactConfig.class);
        this.wxCpService = contactConfig.parse();
    }

    @Async
    public void syncEmployees() throws WxErrorException {
        syncEmployeeDetail();
        // fixme 触发更新联系人 {@link ContactGrabber} 信息
    }

    private void syncEmployeeDetail() throws WxErrorException {
        List<WxCpUser> cpUsers = wxCpService.getUserService().listByDepartment(1L, Boolean.TRUE, 0);
        employeeMapper.insertOrUpdateCpUserDetail(cpUsers);
    }

    void syncDepartments() throws QcBaseException {
//        try {
//            List<WxCpDepart> departs = wxCpService.getDepartmentService().list(null);
//            //fixme 保存部门数据入库
//        } catch (WxErrorException e) {
//            e.printStackTrace();
//            logger.warn("部门同步失败", e);
//            throw new EmployeeException("部门同步失败");
//        }
    }
}
