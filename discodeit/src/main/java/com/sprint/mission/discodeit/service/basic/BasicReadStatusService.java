package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreate;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdate;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;

  @Override
  public ReadStatus create(ReadStatusCreate request) {
    UUID userId = request.userId();
    UUID channelId = request.channelId();
    userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("해당 유저를을 찾을 수 없습니다. "));
    channelRepository.findById(channelId)
        .orElseThrow(() -> new RuntimeException("해당 채널을 찾을 수 없습니다. "));
    return readStatusRepository.findAllByUserId(userId).stream()
        .filter(readStatus -> readStatus.getChannelId().equals(channelId))
        .findFirst()
        .orElseGet(
            () -> {
              Instant lastReadAt = request.lastReadAt();
              ReadStatus readStatus = new ReadStatus(userId, channelId, lastReadAt);
              return readStatusRepository.save(readStatus);
            }
        );
  }

  @Override
  public List<ReadStatus> findAllByUserId(UUID userId) {
    return readStatusRepository.findAllByUserId(userId).stream().toList();

  }

  @Override
  public ReadStatus find(UUID readStatusId) {
    return readStatusRepository.findById(readStatusId)
        .orElseThrow(() -> new RuntimeException("해당 읽음상태를 찾을 수 없습니다."));
  }

  @Override
  public ReadStatus update(UUID readStatusId, ReadStatusUpdate request) {
    Instant newLastReadAt = request.newLastReadAt();
    ReadStatus foundStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(() -> new RuntimeException("해당 채널의 읽음 상태를 찾을 수 없습니다."));
    foundStatus.update(newLastReadAt);
    return readStatusRepository.save(foundStatus);
  }

  @Override
  public void delete(UUID readStatusId) {
    if (!readStatusRepository.existsById(readStatusId)) {
      throw new NoSuchElementException("해당 읽음 상태를 찾을 수 없습니다.");
    }
    readStatusRepository.deleteById(readStatusId);

  }
}
