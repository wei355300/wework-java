package com.qc.wework.contact.external.mapper;

import me.chanjar.weixin.cp.bean.external.contact.ExternalContact;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ContactExternalMapper {

    void insertOrUpdateContactExternal(@Param("list") List<ExternalContact> externalContactList);
}
