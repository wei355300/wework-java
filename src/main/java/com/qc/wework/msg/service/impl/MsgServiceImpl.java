package com.qc.wework.msg.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qc.base.PaginationResponse;
import com.qc.wework.chatdata.service.ChatDataService;
import com.qc.wework.msg.dto.MsgRoom;
import com.qc.wework.msg.dto.MsgRoomContent;
import com.qc.wework.msg.dto.MsgRoomUser;
import com.qc.wework.msg.mapper.MsgMapper;
import com.qc.wework.msg.service.MsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class MsgServiceImpl implements MsgService {

    @Autowired
    private MsgMapper msgMapper;

    @Autowired
    private ChatDataService chatDataService;

    @Override
    public PaginationResponse<MsgRoom> listRooms(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<MsgRoom> list = msgMapper.listRooms();
        PageInfo page = new PageInfo(list);
        return PaginationResponse.toPagination(page);
    }

    @Override
    public PaginationResponse<MsgRoomContent> listContentOfRoom(String roomId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<MsgRoomContent> list = msgMapper.listContentOfRoom(roomId);
        PageInfo page = new PageInfo(list);
        return PaginationResponse.toPagination(page);
    }

    @Override
    public Collection<MsgRoomUser> getMemberListOfRoom(String roomId) {
        return msgMapper.listMembersOfRoom(roomId);
    }

    @Override
    public String getPrimitiveContentByHistoryId(Integer historyId) {
        return msgMapper.getPrimitiveContentByHistoryId(historyId);
    }
}
