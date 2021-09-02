package com.qc.wework.msg.mapper;

import com.qc.wework.msg.dto.MsgRoom;
import com.qc.wework.msg.dto.MsgRoomContent;
import com.qc.wework.msg.dto.MsgRoomUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

@Mapper
public interface MsgMapper {

    List<MsgRoom> listRooms();

    List<MsgRoomUser> listMembersOfRoom(@Param("roomId") String roomId);

    List<MsgRoomContent> listContentOfRoom(@Param("roomId") String roomId);

    String getPrimitiveContentByHistoryId(@Param("historyId") Integer historyId);

    MsgRoomContent getMsgByHistoryId(@Param("historyId") Integer historyId);
}
