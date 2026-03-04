package com.sprint.mission.discodeit.dto.request;

import java.util.UUID;

public record MessageCreate(String content,
                            UUID channelId,
                            UUID authorId) {

}
