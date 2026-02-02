package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.time.Instant;
import java.util.List;

public record ChannelFind(Channel channel, Instant createdAt, List<User> userList) {
}
