package com.qc.wework.contact.service;

import com.qc.wework.contact.dto.Contact;

import java.util.List;
import java.util.Observer;

/**
 * 联系人信息聚合
 *
 * 将内部联系人(员工), 外部联系人, 机器人 等信息, 抽象成统一的联系人信息,
 * 以便于在显示聊天信息中统一展示
 *
 * 采用观察者模式
 * 在与联系人信息相关的数据发生变更时, 触发聚合功能, 将联系人信息聚合
 */
public interface ContactGrabber extends Observer {

    void update(Contact contact);

    void update(List<Contact> contactList);
}
