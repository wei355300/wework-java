package com.qc.wework.chatdata.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.qc.ali.oss.AppendUploader;
import com.qc.ali.oss.Uploader;
import com.qc.ali.oss.UploaderStrategy;
import com.qc.wework.msg.exception.FinanceException;
import com.qc.wework.chatdata.dto.ChatDataParsed;
import com.qc.wework.chatdata.entity.FileEntity;
import com.qc.wework.chatdata.entity.ImageEntity;
import com.qc.wework.chatdata.entity.VideoEntity;
import com.qc.wework.chatdata.entity.VoiceEntity;
import com.qc.wework.chatdata.mapper.ChatDataMapper;
import com.tencent.wework.Finance;
import lombok.Getter;
import lombok.Setter;
import me.chanjar.weixin.cp.api.WxCpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class MediaDataParser extends AbstractChatDataParser {

    private static final Logger logger = LoggerFactory.getLogger(MediaDataParser.class);

    enum UploadType {
        file,
        image,
        voice,
        video
    }

    private static final List<String> UploadTypes;

    static {
        UploadTypes = Stream.of(UploadType.values()).map(Enum::name).collect(Collectors.toList());
    }

    private static final int loopLimit = 10;

    private ChatDataMapper chatDataMapper;

    private UploaderStrategy uploaderStrategy;

    private static Map<String, UploadExec> uploadExecs = new HashMap<>();

    private MediaDataParser(WxCpService wxCpService) {
        super(wxCpService.getWxCpConfigStorage().getCorpId(), wxCpService.getWxCpConfigStorage().getCorpSecret());
    }

    MediaDataParser(WxCpService wxCpService, UploaderStrategy uploaderStrategy, ChatDataMapper chatDataMapper) {
        this(wxCpService);
        this.uploaderStrategy = uploaderStrategy;
        this.chatDataMapper = chatDataMapper;
        uploadExecs.put(FileUploader.type.name(), new FileUploader(uploaderStrategy));
        uploadExecs.put(ImageUploader.type.name(), new ImageUploader(uploaderStrategy));
        uploadExecs.put(VoiceUploader.type.name(), new VoiceUploader(uploaderStrategy));
        uploadExecs.put(VideoUploader.type.name(), new VideoUploader(uploaderStrategy));
    }

    void parse() throws FinanceException {
        parseMediaData();
    }

    /**
     * ???????????? chat_data_parsed ???????????????????????????(??????, ?????????)????????? chat_data_parsed_media ??????
     *
     * @throws FinanceException
     */
    private void parseMediaData() throws FinanceException {
        long financeSdk = -1;
        try {
            financeSdk = geneFinanceSdk();
            setDone(false);
            parseMediaData(financeSdk);
        } catch (FinanceException e) {
            logger.error("fetch media data error", e);
            throw e;
        } finally {
            setDone(true);
            if (financeSdk != -1) {
                freeFinanceSdk(financeSdk);
            }
        }
    }

    /**
     * ????????????????????????, ????????????????????????oss???, ??????????????????????????????
     *
     * @param sdk
     */
    private void parseMediaData(long sdk) {
        //??? chat_data_parsed_media ????????????????????????id, ?????????????????????????????????
        int latestUnParseId = getLatestUnParseId();

        //??? chat_data_parsed ??????????????? chat_data_parsed_media ????????????id?????????, ????????????????????????
        List<ChatDataParsed> mediaItems = getUnParseMedia(latestUnParseId, loopLimit);
        if (CollectionUtils.isEmpty(mediaItems)) {
            return;
        }

        parseAndSaveMedia(sdk, mediaItems);
        // ????????????
        if (mediaItems.size() == 10) {
            parseMediaData(sdk);
        }
    }

    private int getLatestUnParseId() {
        Integer latestHistoryId = chatDataMapper.getLatestParsedIdOfMediaData(UploadTypes);
        return latestHistoryId == null ? 0 : latestHistoryId.intValue();
    }

    private List<ChatDataParsed> getUnParseMedia(int unParseId, int limit) {
        List<ChatDataParsed> items = chatDataMapper.getUnParseMediaItem(unParseId, UploadTypes, limit);
        return Objects.isNull(items) ? Collections.emptyList() : items;
    }

    private void parseAndSaveMedia(long sdk, List<ChatDataParsed> mediaItems) {
        mediaItems.forEach(m -> {
            try{

                String mediaUrl = uploadMedia(sdk, m);
                updateChatDataParsedMediaPath(m.getHistory_id(), mediaUrl);
            }
            catch (FinanceException fe) {
                logger.info("error chat data finance exception, error code {}, msg {} , error", fe.getErrCode(), m.toString());
                if(fe.getErrCode() == 10010) {
                    logger.info("????????????, ????????????");
                    updateChatDataParsedMediaPath(m.getHistory_id(), "");
                }
            }
            catch (Exception e) {
                System.out.println(m.toString());
                logger.error(m.toString());
                logger.error("", e);
            }
        });
    }

    private String uploadMedia(long sdk, ChatDataParsed mediaData) throws FinanceException {
        String msgType = mediaData.getMsgtype();
        return uploadExecs.get(msgType).upload(sdk, mediaData);
    }

    private void updateChatDataParsedMediaPath(int historyId, String mediaUrl) {
        chatDataMapper.updateChatDataMedia(historyId, mediaUrl);
    }
}

abstract class UploadExec {

    private static final Logger logger = LoggerFactory.getLogger(UploadExec.class);

    private static final long sliceSize = 1024 * 512;

    private UploaderStrategy uploaderStrategy;

    UploadExec(UploaderStrategy uploaderStrategy) {
        this.uploaderStrategy = uploaderStrategy;
    }

    String upload(long sdk, ChatDataParsed data) throws FinanceException {
        UploadData uploadData = toUploadData(data);
        String mediaUrl = appendUpload(sdk, uploadData);
        return mediaUrl;
    }

    String appendUpload(long sdk, UploadData uploadData) throws FinanceException {
        Uploader uploader = uploaderStrategy.get(AppendUploader.type);

        String mediaUrl = "";
        try {
            uploader.start(uploadData.getFilePath(), uploadData.getContentType());
            String indexbuf = "";
            long ret = 0 ;

            //????????????GetMediaData???????????????????????????NewMediaData????????????media_data???????????????media_data??????????????????????????????FreeMediaData?????????
            while (true) {
                //????????????GetMediaData???????????????????????????NewMediaData????????????media_data???????????????media_data??????????????????????????????FreeMediaData?????????
                long media_data = Finance.NewMediaData();
                ret = Finance.GetMediaData(sdk, indexbuf, uploadData.getSdkfileid(), AbstractChatDataParser.PROXY, AbstractChatDataParser.PASWD, AbstractChatDataParser.TIMEOUT, media_data);
                //????????????
                if (ret != 0) {
                    Finance.FreeMediaData(media_data);
                    throw new FinanceException((int)ret, "??????????????????");
                }
                byte[] bytes = Finance.GetData(media_data);
                uploader.upload(bytes);

                if (Finance.IsMediaDataFinish(media_data) == 1) {
                    //????????????????????????????????????
                    Finance.FreeMediaData(media_data);
                    mediaUrl = uploader.end();
                    break;
                } else {
                    //?????????????????????????????????indexbuf
                    indexbuf = Finance.GetOutIndexBuf(media_data);
                    Finance.FreeMediaData(media_data);
                }
            }
        }
        catch (FinanceException fe) {
            throw fe;
        }
        catch (Exception e) {
            logger.error("", e);
            uploader.end();
            throw new FinanceException("??????????????????");
        }

        return mediaUrl;
    }

    abstract UploadData toUploadData(ChatDataParsed data);

    <T> T toUploadData(ChatDataParsed data, String type, Class<T> clazz) {
        String content = data.getContent();

        JsonObject jo = JsonParser.parseString(content).getAsJsonObject();
        JsonElement jeType = jo.get(type);
        T fe = (new Gson()).fromJson(jeType, clazz);
        return fe;
    }
}

class FileUploader extends UploadExec {

    static final MediaDataParser.UploadType type = MediaDataParser.UploadType.file;

    FileUploader(UploaderStrategy uploaderStrategy) {
        super(uploaderStrategy);
    }

    UploadData toUploadData(ChatDataParsed data) {
        FileEntity fe = toUploadData(data, type.name(), FileEntity.class);
        UploadData ud = new UploadData();
        ud.setSize(fe.getFilesize());
        ud.setSdkfileid(fe.getSdkfileid());
        ud.setFilePath(fe.getFilename().concat("-") + System.currentTimeMillis() + "." + fe.getFileext());
        ud.setContentType(null);
        return ud;
    }
}

class ImageUploader extends UploadExec {

    static final MediaDataParser.UploadType type = MediaDataParser.UploadType.image;

    ImageUploader(UploaderStrategy uploaderStrategy) {
        super(uploaderStrategy);
    }

    UploadData toUploadData(ChatDataParsed data) {
        ImageEntity fe = toUploadData(data, type.name(), ImageEntity.class);
        UploadData ud = new UploadData();
        ud.setSize(fe.getFilesize());
        ud.setSdkfileid(fe.getSdkfileid());
        ud.setFilePath(fe.getMd5sum() + "" + System.currentTimeMillis() + ".jpg");
        ud.setContentType(null);
        return ud;
    }
}

class VoiceUploader extends UploadExec  {

    static final MediaDataParser.UploadType type = MediaDataParser.UploadType.voice;

    VoiceUploader(UploaderStrategy uploaderStrategy) {
        super(uploaderStrategy);
    }

    UploadData toUploadData(ChatDataParsed data) {
        VoiceEntity fe = toUploadData(data, type.name(), VoiceEntity.class);
        UploadData ud = new UploadData();
        ud.setSize(fe.getVoice_size());
        ud.setSdkfileid(fe.getSdkfileid());
        ud.setFilePath(fe.getMd5sum() + "" + System.currentTimeMillis() + ".amr");
        ud.setContentType(null);
        return ud;
    }
}

class VideoUploader extends UploadExec  {

    static final MediaDataParser.UploadType type = MediaDataParser.UploadType.video;

    VideoUploader(UploaderStrategy uploaderStrategy) {
        super(uploaderStrategy);
    }

    UploadData toUploadData(ChatDataParsed data) {
        VideoEntity fe = toUploadData(data, type.name(), VideoEntity.class);
        UploadData ud = new UploadData();
        ud.setSize(fe.getFilesize());
        ud.setSdkfileid(fe.getSdkfileid());
        ud.setFilePath(fe.getMd5sum() + "" + System.currentTimeMillis() + ".mp4");
        ud.setContentType(null);
        return ud;
    }
}

class UploadData {
    @Getter
    @Setter
    private long size;
    @Getter
    @Setter
    private String sdkfileid;
    @Getter
    @Setter
    private String filePath;
    @Getter
    @Setter
    private String contentType;
}
