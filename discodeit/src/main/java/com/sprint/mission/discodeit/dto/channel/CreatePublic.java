package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.dto.user.UserFind;
import com.sprint.mission.discodeit.entity.User;

public record CreatePublic(String channelName, String description, UserFind user) {

}
