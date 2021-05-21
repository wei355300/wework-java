package com.qc.wework.msg.mapper;

import com.qc.wework.msg.dto.MsgRoom;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MsgMapper {
    List<MsgRoom> list();
}
