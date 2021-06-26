package com.qc.wework.msg.service;

import com.qc.base.PaginationResponse;
import com.qc.wework.msg.dto.MsgRoom;
import com.qc.wework.msg.exception.MsgException;

public interface MsgService {

    PaginationResponse<MsgRoom> listRooms(int pageNum, int pageSize);

//    void triggerSyncChatData() throws MsgException;
}
