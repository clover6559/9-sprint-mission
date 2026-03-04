package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.BinaryContentCreate;
import com.sprint.mission.discodeit.dto.user.UserCreate;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

  User create(UserCreate userCreate, Optional<BinaryContentCreate> binaryContentCreate);

  UserDto find(UUID userId);

  List<UserDto> findAll();

  User update(UUID userId, UserUpdateRequest userUpdateRequest,
      Optional<BinaryContentCreate> binaryContentCreate);

  void delete(UUID id);

}


