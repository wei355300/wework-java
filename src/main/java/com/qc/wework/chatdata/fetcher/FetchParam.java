package com.qc.wework.chatdata.fetcher;

import lombok.Data;

@Data
public class FetchParam {

//    private long sdk;//	        是	    初始化的sdk对象
    private long seq;//	        是	    本次请求获取消息记录开始的seq值。首次访问填写0，非首次使用上次企业微信返回的最大seq。允许从任意seq重入拉取。Uint64类型，范围0-pow(2,64)-1
    private long limit = 100;//	    是	    一次调用限制的limit值，不能超过1000.uint32类型
    private String proxy = null;//	    否	    使用代理的请求，需要传入代理的链接。如：socks5://10.0.0.1:8081 或者 http://10.0.0.1:8081.如不使用代理可以设置为空. 支持sock5跟http代理
    private String paswd = null;//	    否	    代理账号密码，需要传入代理的账号密码。如 user_name:passwd_123
    private long timeout  = 120;//	    是	    超时时长，单位 秒
//    private String priKey;//	是      解密私钥
//    private String corpid;//	是      调用企业的企业id
//    private String corpsecret;//是      平台Secret
}
