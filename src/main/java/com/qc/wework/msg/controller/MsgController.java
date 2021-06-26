package com.qc.wework.msg.controller;

import com.qc.base.PaginationResponse;
import com.qc.base.R;
import com.qc.wework.msg.dto.MsgRoom;
import com.qc.wework.msg.service.MsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/qc/wework/msg")
@Slf4j
public class MsgController {
    @Autowired
    private MsgService msgService;

    /**
     * 按聊天室的方案获取聊天记录
     *
     * @return
     */
    @GetMapping("/room/list")
    public R getMsgList(@RequestParam(name = "current", required = false, defaultValue = "1") int pageNum, @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize) {
        PaginationResponse<MsgRoom> list = msgService.listRooms(pageNum, pageSize);
        return R.suc(list);
    }

    /**
     * 重新同步消息
     * @return
     */
//    @PostMapping("/sync")
//    public R syncMsg() throws MsgException {
//        msgService.triggerSyncChatData();
//        return R.suc();
//    }

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
