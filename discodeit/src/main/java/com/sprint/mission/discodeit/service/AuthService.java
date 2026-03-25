package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.dto.data.LoginDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public interface AuthService {

  UserDto login(LoginDto loginRequest);

  void logout(UUID userId);
}
