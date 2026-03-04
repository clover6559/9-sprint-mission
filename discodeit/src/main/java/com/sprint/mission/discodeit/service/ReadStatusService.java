package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreate;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdate;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

  ReadStatus create(ReadStatusCreate request);

  List<ReadStatus> findAllByUserId(UUID userId);

  ReadStatus find(UUID readStatusId);

  ReadStatus update(UUID readStatusId, ReadStatusUpdate request);

  void delete(UUID id);

}
