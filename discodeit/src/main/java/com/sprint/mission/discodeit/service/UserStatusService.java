package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.UserStatusDto;
import com.sprint.mission.discodeit.dto.request.UserStatusCreate;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdate;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {

  UserStatusDto create(UserStatusCreate request);

  UserStatusDto find(UUID userStatusId);

  List<UserStatusDto> findAll();

  UserStatusDto updateByUserId(UUID userId, UserStatusUpdate update);

  UserStatusDto update(UUID userStatusId, UserStatusUpdate update);

  void delete(UUID userStatusId);

}
