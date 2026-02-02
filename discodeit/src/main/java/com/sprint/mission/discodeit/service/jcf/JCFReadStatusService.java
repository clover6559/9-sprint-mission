package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.ReadStatus.ReadStatusCreate;
import com.sprint.mission.discodeit.dto.ReadStatus.ReadStatusUpdate;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;

import java.util.List;
import java.util.UUID;

public class JCFReadStatusService implements ReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    public JCFReadStatusService(ReadStatusRepository readStatusRepository,UserRepository userRepository,ChannelRepository channelRepository) {
        this.readStatusRepository = readStatusRepository;
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
    }
    @Override
    public ReadStatus create(ReadStatusCreate readStatusCreate) {
        userRepository.findById(readStatusCreate.userId())
                .orElseThrow(() -> new RuntimeException("해당 유저를을 찾을 수 없습니다. "));
        channelRepository.findById(readStatusCreate.channelId())
                .orElseThrow(() -> new RuntimeException("해당 채널을 찾을 수 없습니다. "));
        if (readStatusRepository.existsByChannelIdAndUserId(readStatusCreate.channelId(),readStatusCreate.userId())) {
            throw new RuntimeException("해당 채널에 대한 유저의 읽음 상태가 이미 존재합니다.");
        }
        ReadStatus newStatus = new ReadStatus(readStatusCreate.channelId(), readStatusCreate.userId());
        return readStatusRepository.save(newStatus);
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusRepository.findUserId(userId);

    }

    @Override
    public ReadStatus find(UUID id) {
          return readStatusRepository.findById(id)
                  .orElseThrow(() -> new RuntimeException("해당 읽음상태를 찾을 수 없습니다."));
    }

    @Override
    public ReadStatus update(ReadStatusUpdate readStatusUpdate) {
        ReadStatus foundStatus = readStatusRepository.findById(readStatusUpdate.targetId())
                .orElseThrow(() -> new RuntimeException("해당 읽음 상태를 찾을 수 없습니다."));
        foundStatus.updateLastReadAt(readStatusUpdate.lastReadAt());
        return readStatusRepository.save(foundStatus);
    }

    @Override
    public boolean delete(UUID id) {
        readStatusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 읽음 상태를 찾을 수 없습니다."));
        readStatusRepository.deleteById(id);
        return true;

    }
}
