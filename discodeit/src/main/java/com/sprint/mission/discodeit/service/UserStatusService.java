package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.UserStatusDto;
import com.sprint.mission.discodeit.dto.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {

  UserStatusDto create(UserStatusCreateRequest request);

  UserStatusDto find(UUID userStatusId);

  List<UserStatusDto> findAll();

  UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest update);

  UserStatusDto update(UUID userStatusId, UserStatusUpdateRequest update);

  void delete(UUID userStatusId);

}
