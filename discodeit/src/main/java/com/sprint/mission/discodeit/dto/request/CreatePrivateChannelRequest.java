package com.sprint.mission.discodeit.dto.request;

import java.util.List;
import java.util.UUID;
import jakarta.validation.constraints.*;


public record CreatePrivateChannelRequest(
    @NotEmpty(message = "참여자 목록은 비어있을 수 없습니다.")
    @Size(min = 1, message = "최소 한 명 이상의 참여자가 필요합니다.")
    List<UUID> participantIds) {

}
