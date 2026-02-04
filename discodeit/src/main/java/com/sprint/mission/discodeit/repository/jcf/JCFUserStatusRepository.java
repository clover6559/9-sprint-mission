package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type",havingValue = "jcf", matchIfMissing = true)
public class JCFUserStatusRepository implements UserStatusRepository {
    private final Map<UUID, UserStatus> statusMap = new HashMap<>();

    @Override
    public UserStatus save(UserStatus userStatus) {
        statusMap.put(userStatus.getId(),userStatus);
        return userStatus;
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return statusMap.values().stream()
                .filter(status -> status.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        return statusMap.values().stream()
                .anyMatch(status -> status.getUserId().equals(userId));
    }

    @Override
    public Optional<UserStatus> findById(UUID id) {
        return Optional.ofNullable(statusMap.get(id));
    }

    @Override
    public void deleteByUserId(UUID userId) {
        statusMap.values().removeIf(status -> status.getUserId().equals(userId));
    }

    @Override
    public void deleteById(UUID id) {
        statusMap.remove(id);
    }

    @Override
    public List<UserStatus> findAll() {
        return statusMap.values().stream().toList();
    }
}
