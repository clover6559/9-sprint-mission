package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.dto.loginDto;
import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public interface AuthService {
    User login(loginDto autowired);
    void logout(UUID userId);
}
