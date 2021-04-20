package com.qc.wework.chatdata.controller;

import com.qc.base.R;
import com.qc.msg.exception.FinanceException;
import com.qc.wework.chatdata.service.ChatDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/qc/wework/msg")
public class MsgController {

    private Logger logger = LoggerFactory.getLogger(MsgController.class);

    @Autowired
    private ChatDataService chatDataService;

    /**
     * 重新同步消息
     * @return
     */
    @PostMapping("/sync")
    public R syncMsg() {
        try {
            chatDataService.triggerSyncChatData();
        } catch (FinanceException e) {
            logger.warn("cann't fetch chatdata", e);
            R.fail("2001", e.getMessage());
        }
        return R.suc();
    }

    /**
     * 重新同步消息
     * @return
     */
    @PostMapping("/parse")
    public R parseMsg() {
        try {
            chatDataService.parseChatData();
        } catch (FinanceException e) {
            logger.warn("cann't parse chatdata", e);
            R.fail("2002", e.getMessage());
        }
        return R.suc();
    }

    /**
     * 重新同步消息
     * @return
     */
    @PostMapping("/parse/media")
    public R parseMedia() {
        try {
            chatDataService.parseMediaData();
        } catch (FinanceException e) {
            logger.warn("cann't parse media", e);
            R.fail("2003", e.getMessage());
        }
        return R.suc();
    }

    /**
     * 重新同步消息
     * @return
     */
    @GetMapping("/sync/result")
    public R GetSyncMsgResult() {
        return R.suc();
    }
}
