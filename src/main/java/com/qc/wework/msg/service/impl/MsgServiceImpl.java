package com.qc.wework.msg.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qc.ali.codec.TransferFailureException;
import com.qc.ali.codec.VoiceTransCodec;
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

import java.net.MalformedURLException;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class MsgServiceImpl implements MsgService {

    private MsgMapper msgMapper;
    private ChatDataService chatDataService;
    private VoiceTransCodec voiceTransCodec;

    @Autowired
    public MsgServiceImpl(MsgMapper msgMapper, ChatDataService chatDataService, VoiceTransCodec voiceTransCodec) {
        this.msgMapper = msgMapper;
        this.chatDataService = chatDataService;
        this.voiceTransCodec = voiceTransCodec;
    }

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

    @Override
    public String transVoiceFormat(int msgId) throws TransferFailureException {
        MsgRoomContent msg = msgMapper.getMsgByHistoryId(msgId);
        String voiceUrl = msg.getContent();
        return transVoiceFormat(voiceUrl);
    }

    @Override
    public String getVoicePlayAddress(int msgId, boolean transfer) throws TransferFailureException {
        MsgRoomContent msg = msgMapper.getMsgByHistoryId(msgId);
        String voiceUrl = msg.getContent();
        boolean isTrans = false;
        try {
            isTrans = voiceTransCodec.isTrans(voiceUrl);
        } catch (MalformedURLException e) {
            log.warn("", e);
            throw new TransferFailureException("");
        }
        if (isTrans) {
            return voiceTransCodec.getPlayAddress(voiceUrl);
        }
        return transVoiceFormat(voiceUrl);
    }

    private String transVoiceFormat(String voiceUrl) throws TransferFailureException {
        try {
            return voiceTransCodec.transCodec(voiceUrl, VoiceTransCodec.Content_Type_MP3);
        } catch (TransferFailureException e) {
            log.warn("", e);
            throw e;
        } catch (MalformedURLException e) {
            log.warn("", e);
        }
        throw new TransferFailureException("");
    }
}
