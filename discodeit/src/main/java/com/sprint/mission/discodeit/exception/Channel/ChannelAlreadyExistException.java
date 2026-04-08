package com.sprint.mission.discodeit.exception.Channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.UUID;

public class ChannelAlreadyExistException extends ChannelException {

    public ChannelAlreadyExistException(UUID channelId) {
        super(ErrorCode.DUPLICATE_CHANNEL, String.format("이미 존재하는 채널입니다: ID=%s", channelId));
        addDetails("channelId", channelId);
        addDetails("searchType", "byId");
    }

    public ChannelAlreadyExistException(String name) {
        super(ErrorCode.DUPLICATE_CHANNEL, String.format("이미 사용 중인 이름입니다: %s", name));
        addDetails("name", name);
        addDetails("searchType", "byName");
    }
}
