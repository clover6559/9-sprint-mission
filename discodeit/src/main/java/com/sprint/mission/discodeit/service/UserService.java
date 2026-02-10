package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.UserCreate;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserFind;
import com.sprint.mission.discodeit.dto.user.UserUpdate;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.search.UserSearch;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User create(UserCreate userCreate);
    UserFind find(UUID userId);
    List<UserFind> search(UserSearch userSearch);
    List<UserDto> findAll();
    void update(UserUpdate userUpdate);
    boolean delete(UUID id);
    void printRemainUsers();

    }


