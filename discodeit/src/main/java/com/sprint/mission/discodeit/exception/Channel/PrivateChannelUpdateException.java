package com.sprint.mission.discodeit.exception.Channel;

import com.sprint.mission.discodeit.entity.Channel.ChannelType;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class PrivateChannelUpdateException extends ChannelException {

    public PrivateChannelUpdateException(ChannelType channelType) {
        super(
                ErrorCode.PRIVATE_CHANNEL_UPDATE,
                String.format("비공개 채널은 수정할 수 없습니다: channelType=%s", channelType));
        addDetails("channelType", channelType);
        addDetails("searchType", "byChannelType");
    }
}
