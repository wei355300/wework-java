package com.qc.wework.chatdata.service.impl;


import com.qc.msg.exception.FinanceException;
import com.qc.wework.chatdata.mapper.ChatDataMapper;
import com.tencent.wework.Finance;
import com.qc.wework.chatdata.dto.ChatDataItem;
import com.qc.wework.chatdata.dto.ChatDataParsed;
import me.chanjar.weixin.cp.api.WxCpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

class ChatDataParser extends AbstractChatDataParser {

    private static final Logger logger = LoggerFactory.getLogger(ChatDataParser.class);

    private ChatDataMapper chatDataMapper;

    private ChatDataParser(WxCpService wxCpService) {
        super(wxCpService);
    }

    ChatDataParser(WxCpService wxCpService, ChatDataMapper chatDataMapper) {
        this(wxCpService);
        this.chatDataMapper = chatDataMapper;
    }

    void parse() throws FinanceException {
        parseChatData();
    }

    /**
     * 将数据库chat_data_history中未解析的原始数据解析到chat_data_content表中
     *
     * 从 chat_data_content 查找最后一个history_id记录后
     * 从chat_data_history基于该history_id往后查数据, 查出的数据都表示为未解析
     * {@code #loadChatData}
     *
     * @throws FinanceException
     */
    private void parseChatData() throws FinanceException {
        long financeSdk = -1;
        try {
            financeSdk  = geneFinanceSdk();
            setDone(false);
            parseChatData(financeSdk);
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

    private void parseChatData(long sdk) {
        //从chat_data_content取得最后一个history_id
        int historyId = getLatestUnParseId();
        logger.info("latest parsed msg id: {}", historyId);
        List<ChatDataItem> chatDataItems = getUnParseChatData(historyId, 10);
        if (CollectionUtils.isEmpty(chatDataItems)) {
            logger.debug("chat data items was empty");
            return;
        }
        parseAndSaveChatDataItems(sdk, chatDataItems);
        // 递归解析
        if (chatDataItems.size() == 10) {
            logger.debug("has more un parse chat data msg, continue...");
            parseChatData(sdk);
        }
    }

    private void parseAndSaveChatDataItems(long sdk, List<ChatDataItem> chatDataItems) {
        logger.debug("分析会话存档内容并保存...");
        List<ChatDataParsed> chatDataContents = new ArrayList<>(chatDataItems.size());
        for (ChatDataItem item : chatDataItems) {
            long slice = Finance.NewSlice();
            try {
                String encryptKey = RSA.decryptRSA(item.getEncrypt_random_key());
                long stat = Finance.DecryptData(sdk, encryptKey, item.getEncrypt_chat_msg(), slice);
                if (stat != 0) {
                    logger.warn("ChatData:{}解码出现问题：", item);
                }

                String resultContent = Finance.GetContentFromSlice(slice);
                logger.debug("解析结果: {}", resultContent);

                ChatDataParsed chatDataParsed = Parser.toChatDataParsed(item.getId(), item.getMsgid(), resultContent);

                chatDataContents.add(chatDataParsed);

            } catch (Exception e) {
                logger.warn("分析数据出错: ", e);
                continue;
            }
        }
        saveChatDataItemContent(chatDataContents);
    }

    private void saveChatDataItemContent(List<ChatDataParsed> chatDataParseds) {
        logger.debug("保存解析结果");
        chatDataMapper.insertChatDataParsed(chatDataParseds);
    }

    private List<ChatDataItem> getUnParseChatData(int historyId, int limit) {
        List<ChatDataItem> items = chatDataMapper.getUnParseChatDataItem(historyId, limit);
        return Objects.isNull(items) ? Collections.emptyList() : items;
    }

    /**
     * 获取最后一个被解析的会话记录的ID
     * @return
     */
    private int getLatestUnParseId() {
        Integer latestHistoryId = chatDataMapper.getLatestParsedIdOfChatData();
        logger.debug("get latest history id is {}", latestHistoryId);
        //如果content中没有数据, 默认从history表开始解析
        return latestHistoryId == null ? 0 : latestHistoryId.intValue();
    }
}
