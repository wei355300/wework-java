package com.qc.wework.msg.controller;

import com.qc.ali.codec.CodecFailureException;
import com.qc.base.R;
import com.qc.wework.msg.service.MsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public R tranVoiceToMp3(@RequestParam(name = "msgId") int msgId) throws CodecFailureException {
        String url = msgService.transVoiceFormat(msgId);
        return R.suc(url);
    }

}
