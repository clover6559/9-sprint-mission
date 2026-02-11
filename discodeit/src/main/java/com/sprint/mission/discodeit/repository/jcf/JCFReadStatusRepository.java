package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;
@Repository
@ConditionalOnProperty(name = "discodeit.repository.type",havingValue = "jcf", matchIfMissing = true)
public class JCFReadStatusRepository implements ReadStatusRepository {
    private final Map<UUID, ReadStatus> readStatusMap = new HashMap<>();

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        readStatusMap.put(readStatus.getId(), readStatus);
        return readStatus;
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        return Optional.ofNullable(readStatusMap.get(id));
    }

    @Override
    public List<ReadStatus> findUserId(UUID userId) {
        return readStatusMap.values().stream()
                .filter(readStatus -> readStatus.getUserId().equals(userId))
                .toList();
    }

    @Override
    public List<ReadStatus> findByChannelId(UUID channelId) {
        return readStatusMap.values().stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public List<ReadStatus> findAll() {
        return readStatusMap.values().stream().toList();
    }

    @Override
    public boolean existsByChannelIdAndUserId(UUID channelId, UUID userId) {
        return readStatusMap.values().stream()
                .anyMatch(readStatus ->
                        channelId.equals(readStatus.getChannelId()) &&
                        readStatus.getUserId() != null &&                       readStatus.getChannelId().equals(channelId) &&
                        readStatus.getUserId().equals(userId)
                );
    }

    @Override
    public void deleteById(UUID id) {
        readStatusMap.remove(id);
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        readStatusMap.values().removeIf(readStatus -> readStatus.getChannelId().equals(channelId));

    }
}
