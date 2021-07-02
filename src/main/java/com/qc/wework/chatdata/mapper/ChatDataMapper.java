package com.qc.wework.chatdata.mapper;

import com.qc.wework.chatdata.dto.ChatDataItem;
import com.qc.wework.chatdata.dto.ChatDataParsed;
import com.qc.wework.chatdata.dto.MediaContent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatDataMapper {


    Integer getLatestSeq();

    /**
     * 获取最后一个被解析的会话记录的ID
     * @return
     */
    Integer getLatestParsedIdOfChatData();

    Integer getLatestParsedIdOfMediaData(@Param("mediaTypes") List<String> mediaTypes);

    List<ChatDataParsed> getParsedChatDataItem(@Param("historyId") int preHistoryId, @Param("limit") int limit);

    List<ChatDataItem> getUnParseChatDataItem(@Param("historyId") int historyId, @Param("limit") int limit);

    List<ChatDataParsed> getUnParseMediaItem(@Param("historyId") int historyId, @Param("mediaTypes") List<String> mediaTypes, @Param("limit") int limit);

    ChatDataParsed getChatDataParsedByHistoryId(@Param("historyId") int historyId);

    void insertChatDataItem(@Param("chatDataItemList") List<ChatDataItem> chatdata);

    void insertChatDataParsed(@Param("chatDataParsedList") List<ChatDataParsed> chatDataParseds);

    void insertChatDataSender(@Param("chatDataParsedList") List<ChatDataParsed> chatDataParseds);

    int updateChatDataMedia(@Param("historyId") int historyId, @Param("mediaUrl") String mediaUrl);

//    void insertChatDataRoomShip(@Param("chatDataParsedList") List<ChatDataParsed> chatDataParseds);

    void insertChatDataRoomUser(@Param("chatDataParsedList") List<ChatDataParsed> chatDataParseds);

    int getHistoryIdOFLastedParsedRoomUser();

    int getHistoryIdOFLastedParsedSender();
}
