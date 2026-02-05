package com.sprint.mission.discodeit.dto.message;

import java.time.Instant;
import java.util.UUID;

public record MessageResponse(
        UUID userId, String userName, String channelName, String content, Instant createAt, Instant updateAt) {
    @Override
    public String toString() {
        return """
                [메세지 정보]
                보낸 사람  : %s
                채널 이름 : %s
                내용     : %s
                생성 시간 : %s
                수정 시간 : %s
                """.formatted(
                userName,
                channelName,
                content,
                createAt,
                updateAt
        );
    }
}
