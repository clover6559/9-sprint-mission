package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.dto.user.UserFind;

public record CreatePublic(String name, String description, UserFind user) {

}
