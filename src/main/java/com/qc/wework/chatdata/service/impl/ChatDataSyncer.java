package com.qc.wework.chatdata.service.impl;


import com.qc.wework.msg.exception.FinanceException;
import com.qc.utils.JsonUtils;
import com.qc.wework.chatdata.dto.ChatData;
import com.qc.wework.chatdata.mapper.ChatDataMapper;
import com.tencent.wework.Finance;
import me.chanjar.weixin.cp.api.WxCpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

class ChatDataSyncer extends AbstractChatDataSync {

    private static final Logger logger = LoggerFactory.getLogger(ChatDataSyncer.class);

    private ChatDataMapper chatDataMapper;

    private ChatDataSyncer(String corpId, String corpSecret) {
        super(corpId, corpSecret);
    }

    ChatDataSyncer(WxCpService wxCpService, ChatDataMapper chatDataMapper) {
        this(wxCpService.getWxCpConfigStorage().getCorpId(), wxCpService.getWxCpConfigStorage().getCorpSecret());
        this.chatDataMapper = chatDataMapper;
    }

    void sync() throws FinanceException {
        fetchChatData();
    }

    private void fetchChatData() throws FinanceException {
        long financeSdk = -1;
        try {
            financeSdk  = geneFinanceSdk();
            setDone(false);
            fetchAndSaveOriData(financeSdk);
        } catch (FinanceException e) {
            logger.error("fetch data error", e);
            throw e;
        }
        finally {
            setDone(true);
            if(financeSdk != -1) {
                freeFinanceSdk(financeSdk);
            }
        }
    }



    /**
     * 递归拉取数据, 直到拉取的chatdata数据为null
     * @param sdk
     * @throws FinanceException
     */
    private void fetchAndSaveOriData(long sdk) throws FinanceException {
        long slice = Finance.NewSlice();
        try {
            long ret = Finance.GetChatData(
                    sdk,
                    getLatestSeq(),
                    LIMIT,
                    PROXY,
                    PASWD,
                    TIMEOUT,
                    slice
            );
            if (ret != 0) {
                throw new FinanceException("拉取数据失败");
            }
            String msg = Finance.GetContentFromSlice(slice);
            logger.info("get content from slice: {}", msg);

            ChatData chatData = parseOriData(msg);
            logger.info("parse msg result: ", chatData.getErrCode());
            if (Objects.isNull(chatData.getChatdata()) || chatData.getChatdata().isEmpty()) {
                logger.info("chatdata is null, return");
                return;
            }
            saveChatDataItem(chatData);
        } catch (FinanceException f) {
            logger.error("get chatdata err", f);
            throw f;
        } catch (Exception e) {
            logger.error("get chatdata error unknow", e);
            throw new FinanceException(e.getMessage());
        } finally {
            Finance.FreeSlice(slice);
        }

        fetchAndSaveOriData(sdk);
    }

    /**
     * 获取同步的最后一个id
     * @return
     */
    private int getLatestSeq() {
        Integer latestSeq = chatDataMapper.getLatestSeq();
        logger.info("get latest seq is {}", latestSeq);
        return latestSeq == null ? 0 : latestSeq.intValue();
    }

    private void saveChatDataItem(ChatData chatData) {
        logger.info("save chat data into item table");
        chatDataMapper.insertChatDataItem(chatData.getChatdata());
    }

    private ChatData parseOriData(String msg) {
        logger.info("parse original data: {}", msg);
        ChatData chatData = JsonUtils.parser(msg, ChatData.class);
        return chatData;
    }
}
