package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ReadStatus.ReadStatusCreate;
import com.sprint.mission.discodeit.dto.ReadStatus.ReadStatusUpdate;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatus create(ReadStatusCreate readStatusCreate);
    List<ReadStatus> findAllByUserId(UUID userId);
    ReadStatus findById(UUID id);
    ReadStatus update(ReadStatusUpdate readStatusUpdate);
    boolean delete(UUID id);

}
