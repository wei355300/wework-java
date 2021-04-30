package com.qc.wework.chatdata;

public interface Msg {

    interface Type {
        String TEXT = "text";//文本
        String IMAGE = "image";//图片
        String REVOKE = "revoke";//撤回消息
        String AGREE  = "agree"; //同意会话聊天内容
        String DISAGREE  = "disagree";//不同意会话聊天内容
        String VOICE  = "voice";//语音
        String VIDEO  = "video";//视频
        String CARD  = "card";//名片
        String LOCATION  = "location";//位置
        String EMOTION  = "emotion";//表情
        String FILE  = "file";//文件
        String LINK  = "link";//链接
        String WEAPP  = "weapp";//小程序
        String CHATRECORD  = "chatrecord";//会话记录消息
        String CHATRECORD_ITEM  = "ChatRecord";//会话记录消息
        String TODO  = "todo";//待办消息
        String VOTE  = "vote";//投票消息
        String COLLECT  = "collect";//填表消息
        String REDPACKET  = "redpacket";//红包消息
        String MEETING  = "";//会议邀请消息
        String CHANGE_ENTERPRISE_LOG  = "change_enterprise_log";//切换企业日志  ********微信不提供msgtype字段********
        String DOCMSG  = "docmsg";//在线文档消息
        String MARKDOWN  = "markdown";//MarkDown格式消息
        String NEWS  = "news";//图文消息
        String CALENDAR  = "calendar";//日程消息
        String MIXED  = "mixed";//混合消息
        String MEETING_VOICE_CALL  = "meeting_voice_call";//音频存档消息
        String VOIP_DOC_SHARE  = "voip_doc_share";//音频共享文档消息
    }

    interface Prop {
        String ACTION = "action";
        String FROM   = "from";
        String TOLIST = "tolist";
        String ROOMID = "roomid";
        String MSGTIME = "msgtime";
        String MSGTYPE = "msgtype";
        String CONTENT = "content";
    }

    interface Action {
        String SEND = "send";
    }
}
