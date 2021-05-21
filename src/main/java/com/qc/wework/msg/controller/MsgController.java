package com.qc.wework.msg.controller;

import com.qc.base.PaginationRequest;
import com.qc.base.PaginationResponse;
import com.qc.base.R;
import com.qc.wework.msg.dto.MsgRoom;
import com.qc.wework.msg.exception.MsgException;
import com.qc.wework.msg.service.MsgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/qc/wework/msg")
public class MsgController {

    private Logger logger = LoggerFactory.getLogger(MsgController.class);

    @Autowired
    private MsgService msgService;

    /**
     * 按聊天室的方案获取聊天记录
     * @return
     */
    @GetMapping("/room/list")
    public R getMsgList(@Valid PaginationRequest params) {
        PaginationResponse<MsgRoom> list = msgService.list(params.getCurrent(), params.getPageSize());
        return R.suc(list);
    }

    /**
     * 重新同步消息
     * @return
     */
    @PostMapping("/sync")
    public R syncMsg() throws MsgException {
        msgService.triggerSyncChatData();
        return R.suc();
    }

//    /**
//     * 重新同步消息
//     * @return
//     */
//    @PostMapping("/parse")
//    public R parseMsg() {
//        try {
//            chatDataService.parseChatData();
//        } catch (FinanceException e) {
//            logger.warn("cann't parse chatdata", e);
//            R.fail("2002", e.getMessage());
//        }
//        return R.suc();
//    }
//
//    /**
//     * 重新同步消息
//     * @return
//     */
//    @PostMapping("/parse/media")
//    public R parseMedia() {
//        try {
//            chatDataService.parseMediaData();
//        } catch (FinanceException e) {
//            logger.warn("cann't parse media", e);
//            R.fail("2003", e.getMessage());
//        }
//        return R.suc();
//    }
//
//    /**
//     * 重新同步消息
//     * @return
//     */
//    @GetMapping("/sync/result")
//    public R GetSyncMsgResult() {
//        return R.suc();
//    }
}
