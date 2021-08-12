package com.qc.wework.msg.controller;

import com.qc.base.PaginationRequest;
import com.qc.base.PaginationResponse;
import com.qc.base.R;
import com.qc.wework.msg.dto.MsgRoom;
import com.qc.wework.msg.dto.MsgRoomContent;
import com.qc.wework.msg.dto.MsgRoomUser;
import com.qc.wework.msg.service.MsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

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
     * 获取聊天室的内容
     *
     * @return
     */
    @GetMapping("/room/{roomId}/content")
    public R getContentOfRoom(@PathVariable String roomId, @Valid  PaginationRequest paginationParams) {
        PaginationResponse<MsgRoomContent> list = msgService.listContentOfRoom(roomId, paginationParams.getCurrent(), paginationParams.getPageSize());
        return R.suc(list);
    }

    /**
     * 获取聊天记录的原始内容
     *
     * @return json 格式的字符串
     */
    @GetMapping("/detail/{historyId}")
    public R getPrimitiveContentOfHistoryMsg(@PathVariable Integer historyId) {
        String msgDetail = msgService.getPrimitiveContentByHistoryId(historyId);
        return R.suc(msgDetail);
    }

    /**
     * 获取聊天室的成员
     *
     * @return
     */
    @GetMapping("/room/{roomId}/members")
    public R getMemberListOfRoom(@PathVariable String roomId) {
        Collection<MsgRoomUser> list = msgService.getMemberListOfRoom(roomId);
        return R.suc(list);
    }
}
