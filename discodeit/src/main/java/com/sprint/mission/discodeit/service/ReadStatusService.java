package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.ReadStatusDto;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreate;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdate;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

  ReadStatusDto create(ReadStatusCreate request);

  List<ReadStatusDto> findAllByUserId(UUID userId);

  ReadStatusDto find(UUID readStatusId);

  ReadStatusDto update(UUID readStatusId, ReadStatusUpdate request);

  void delete(UUID id);

}
