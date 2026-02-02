package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
public class JCFUserStatusRepository implements UserStatusRepository {
    private final Map<UUID, UserStatus> statusMap = new HashMap<>();

    @Override
    public void save(UserStatus userStatus) {
        statusMap.put(userStatus.getUserId(),userStatus);
    }

    @Override
    public UserStatus findByUserId(UUID userId) {
        return statusMap.get(userId);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        statusMap.remove(userId);
    }
}
