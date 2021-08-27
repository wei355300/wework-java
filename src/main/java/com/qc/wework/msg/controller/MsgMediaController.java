package com.qc.wework.msg.controller;

import com.qc.ali.codec.TransferFailureException;
import com.qc.base.R;
import com.qc.wework.msg.service.MsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/qc/wework/msg/media")
@Slf4j
public class MsgMediaController {
    @Autowired
    private MsgService msgService;

    /**
     * 音频文件转码
     * <p>
     * 将音频文件转为mp3格式
     *
     * @return
     */
    @PutMapping("/voice/mp3")
    public R tranVoiceToMp3(@RequestParam(name = "msgId") int msgId) throws TransferFailureException {
        String url = msgService.transVoiceFormat(msgId);
        return R.suc(url);
    }

    /**
     *
     * @param msgId
     * @return
     * @throws TransferFailureException
     */
    @GetMapping("/voice/mp3")
    public R getPlayAddress(@RequestParam(name = "msgId", required = true) int msgId, @RequestParam(name="trans", defaultValue = "0") boolean transfer) throws TransferFailureException {
        String url = msgService.getVoicePlayAddress(msgId, transfer);
        return R.suc(url);
    }

}
