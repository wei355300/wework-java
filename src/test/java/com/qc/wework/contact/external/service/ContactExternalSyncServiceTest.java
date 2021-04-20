package com.qc.wework.contact.external.service;

import me.chanjar.weixin.common.error.WxErrorException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ContactExternalSyncServiceTest {

    @Autowired
    private ContactExternalSyncService contactExternalSyncService;

    @Test
    public void testSyncExternalContacts() throws WxErrorException {
        contactExternalSyncService.syncExternalContacts();
    }
}
