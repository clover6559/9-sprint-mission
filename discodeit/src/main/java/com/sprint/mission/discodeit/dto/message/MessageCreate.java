package com.sprint.mission.discodeit.dto.message;

import java.util.UUID;

public record MessageCreate(String content,
                            UUID channelId,
                            UUID authorId) {

}
