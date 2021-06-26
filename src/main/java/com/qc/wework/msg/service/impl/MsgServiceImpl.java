package com.qc.wework.msg.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qc.base.PaginationResponse;
import com.qc.wework.chatdata.service.ChatDataService;
import com.qc.wework.msg.dto.MsgRoom;
import com.qc.wework.msg.exception.FinanceException;
import com.qc.wework.msg.exception.MsgException;
import com.qc.wework.msg.mapper.MsgMapper;
import com.qc.wework.msg.service.MsgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MsgServiceImpl implements MsgService {

    private static final Logger logger = LoggerFactory.getLogger(MsgServiceImpl.class);

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

//    @Override
//    public void triggerSyncChatData() throws MsgException {
//        try {
//            chatDataService.triggerSyncChatData();
//        } catch (FinanceException e) {
//            logger.warn("触发同步消息失败!", e);
//            throw new MsgException();
//        }
//    }

//    private ChatDataService chatDataService;
//
//    public MsgServiceImpl(@Autowired ChatDataService chatDataService) {
//        this.chatDataService = chatDataService;
//    }
//
//    @Override
//    public void syncMsg() throws FinanceException {
//        if (ChatDataServiceImpl.isFetcher()) {
//            throw new FinanceException("拉取任务仍在执行, 请稍等!");
//        }
//        chatDataService.fetchDataFromWeWork();
//
//    }
//
//    @Override
//    public void parseMsg() throws FinanceException {
//        if (ChatDataServiceImpl.isFetcher()) {
//            throw new FinanceException("拉取任务仍在执行, 请稍等!");
//        }
//        chatDataService.parseChatData();
//    }
//
//    @Override
//    public void parseMedia() throws FinanceException {
//        if (ChatDataServiceImpl.isFetcher()) {
//            throw new FinanceException("拉取任务仍在执行, 请稍等!");
//        }
//        chatDataService.parseMediaData();
//    }
}
