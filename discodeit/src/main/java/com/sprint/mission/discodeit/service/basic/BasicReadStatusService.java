package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.ReadStatusDto;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.Channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.ReadStatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.exception.User.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final ReadStatusMapper readStatusMapper;

    @Transactional
    @Override
    public ReadStatusDto create(ReadStatusCreateRequest request) {
        User user =
                userRepository
                        .findById(request.userId())
                        .orElseThrow(() -> new UserNotFoundException(request.userId()));
        Channel channel =
                channelRepository
                        .findById(request.channelId())
                        .orElseThrow(() -> new ChannelNotFoundException(request.channelId()));
        ReadStatus readStatus =
                readStatusRepository
                        .findByUserIdAndChannelId(request.userId(), request.channelId())
                        .orElseGet(
                                () -> {
                                    Instant lastReadAt = request.lastReadAt();
                                    ReadStatus newReadStatus =
                                            new ReadStatus(user, channel, lastReadAt);
                                    return readStatusRepository.save(newReadStatus);
                                });
        return readStatusMapper.toDto(readStatus);
    }

    @Override
    public List<ReadStatusDto> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId).stream()
                .map(readStatusMapper::toDto)
                .toList();
    }

    @Override
    public ReadStatusDto find(UUID readStatusId) {
        return readStatusRepository
                .findById(readStatusId)
                .map(readStatusMapper::toDto)
                .orElseThrow(() -> new ReadStatusNotFoundException(readStatusId));
    }

    @Transactional
    @Override
    public ReadStatusDto update(UUID readStatusId, ReadStatusUpdateRequest request) {
        Instant newLastReadAt = request.newLastReadAt();
        ReadStatus foundStatus =
                readStatusRepository
                        .findById(readStatusId)
                        .orElseThrow(() -> new ReadStatusNotFoundException(readStatusId));
        foundStatus.update(newLastReadAt);
        return readStatusMapper.toDto(foundStatus);
    }

    @Transactional
    @Override
    public void delete(UUID readStatusId) {
        if (!readStatusRepository.existsById(readStatusId)) {
            throw new ReadStatusNotFoundException(readStatusId);
        }
        readStatusRepository.deleteById(readStatusId);
    }
}
