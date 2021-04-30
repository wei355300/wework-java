package com.qc.wework.contact.external.service;

import com.qc.config.ConfigService;
import com.qc.utils.JsonUtils;
import com.qc.wework.contact.ContactConfig;
import com.qc.wework.contact.external.mapper.ContactExternalMapper;
import com.qc.wework.employee.dto.Employee;
import com.qc.wework.employee.service.EmployeeService;
import lombok.Getter;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.external.contact.ExternalContact;
import me.chanjar.weixin.cp.bean.external.contact.WxCpExternalContactBatchInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class ContactExternalSyncService {

    private Logger logger = LoggerFactory.getLogger(ContactExternalSyncService.class);

    private interface Cfg {
        String MODULE = "wechat_cp_app";
        String CODE = "contact";

        int PRE_EMPLOYEE_LIMIT = 5;
        int PRE_CONTACT_EXTERNAL_LIMIT = 50;

    }


    @Getter
    private WxCpService wxCpService;

    @Autowired
    private ContactExternalMapper contactExternalMapper;

    @Autowired
    private EmployeeService employeeService;

    private ContactExternalSyncService(@Autowired ConfigService configService) {
        toWxCpService(configService);
    }

    private void toWxCpService(ConfigService configService) {
        String configs = configService.getConfig(Cfg.MODULE, Cfg.CODE);
        ContactConfig contactConfig = JsonUtils.parser(configs, ContactConfig.class);
        this.wxCpService = contactConfig.parse();
    }

    public void syncExternalContacts() {
        syncByEmployees();
    }

    /**
     * 全量同步外部联系人
     *
     * @throws WxErrorException
     */
    private void syncByEmployees() {
        //分片获取员工信息,
        //按员工同步该员工添加的外部联系人
        syncByEmployees(0, Cfg.PRE_EMPLOYEE_LIMIT);
    }

    private void syncByEmployees(int offset, int limit) {
        Collection<Employee> employees = employeeService.listEmployeesByLimit(offset, limit);
        if (CollectionUtils.isEmpty(employees)) {
            return;
        }
        employees.forEach(employee -> {
            try {
                logger.info("开始同步员工 {} 的外部联系人...", employee.getName());
                syncContactExternalOfEmployee(employee, null, Cfg.PRE_CONTACT_EXTERNAL_LIMIT);
                logger.info("员工 {} 的外部联系人同步完成.", employee.getName());
            } catch (WxErrorException we) {
                logger.warn("同步外部联系人失败, 员工:{}, 失败信息{}", employee.getName(), we.getMessage());
                logger.error("", we);
            }
        });
        if (employees.size() == limit) {
            syncByEmployees(offset + limit, limit);
        }
    }

    private void syncContactExternalOfEmployee(Employee employee, String cursor, int limit) throws WxErrorException {
        WxCpExternalContactBatchInfo  batchInfo = wxCpService.getExternalContactService().getContactDetailBatch(employee.getUserId(), cursor, limit);
        List<WxCpExternalContactBatchInfo.ExternalContactInfo> externalContactInfos = batchInfo.getExternalContactList();
        if (CollectionUtils.isEmpty(externalContactInfos)) {
            return;
        }
        saveOrUpdateContactExternal(externalContactInfos);

        // 如果返回的结果数量与limit数量不一致, 说明有更多的数据需要同步
        if (externalContactInfos.size() == limit) {
            syncContactExternalOfEmployee(employee, batchInfo.getNextCursor(), limit);
        }
    }

    private void saveOrUpdateContactExternal(List<WxCpExternalContactBatchInfo.ExternalContactInfo> externalContactInfos) {
        List<ExternalContact> externalContactList = new ArrayList<>();
        externalContactInfos.forEach(ec -> {
            ExternalContact externalContact = ec.getExternalContact();
            externalContactList.add(externalContact);
        });
        contactExternalMapper.insertOrUpdateContactExternal(externalContactList);
    }
}
