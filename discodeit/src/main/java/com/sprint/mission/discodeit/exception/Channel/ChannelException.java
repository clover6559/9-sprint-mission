package com.sprint.mission.discodeit.exception.Channel;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
//TODO 비공개 채널 생성 시 유저 없으면 생성 불가 예외 만들어서 등록

public class ChannelException extends DiscodeitException {

  protected ChannelException(ErrorCode errorCode) {
    super(errorCode);
  }

  protected ChannelException(ErrorCode errorCode, String customMessage) {
    super(errorCode, customMessage);
  }
}
