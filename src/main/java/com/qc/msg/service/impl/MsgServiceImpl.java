package com.qc.msg.service.impl;

import com.qc.wework.chatdata.service.ChatDataService;
import com.qc.msg.exception.FinanceException;
import com.qc.wework.chatdata.service.impl.ChatDataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MsgServiceImpl {

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
