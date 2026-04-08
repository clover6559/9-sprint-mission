package com.sprint.mission.discodeit.exception.Channel;

import com.sprint.mission.discodeit.entity.Channel.ChannelType;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class EmptyParticipantListException extends ChannelException {

  public EmptyParticipantListException(ChannelType type) {
    super(ErrorCode.EMPTY_PARTICIPANT_LIST,
        String.format("[%s] 타입 채널은 최소 1명 이상의 참여자가 필요합니다.", type));

    addDetails("channelType", type);
    addDetails("reason", "At least one participant is required for a private channel.");
  }
}
