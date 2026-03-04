package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.UserStatusCreate;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdate;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {

  UserStatus create(UserStatusCreate request);

  UserStatus find(UUID userStatusId);

  List<UserStatus> findAll();

  UserStatus updateByUserId(UUID userId, UserStatusUpdate update);

  UserStatus update(UUID userStatusId, UserStatusUpdate update);

  void delete(UUID userStatusId);

}
