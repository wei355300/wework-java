package com.qc.wework.msg.service;

import com.qc.base.PaginationResponse;
import com.qc.wework.msg.dto.MsgRoom;
import com.qc.wework.msg.dto.MsgRoomContent;
import com.qc.wework.msg.dto.MsgRoomUser;
import com.qc.wework.msg.exception.MsgException;

import java.util.Collection;

public interface MsgService {

    PaginationResponse<MsgRoom> listRooms(int pageNum, int pageSize);

    PaginationResponse<MsgRoomContent> listContentOfRoom(String roomId, int pageNum, int pageSize);

    Collection<MsgRoomUser> getMemberListOfRoom(String roomId);

//    void triggerSyncChatData() throws MsgException;
}
