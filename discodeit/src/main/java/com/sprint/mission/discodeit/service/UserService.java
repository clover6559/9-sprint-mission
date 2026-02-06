package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.UserCreate;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.dto.user.UserUpdate;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.search.UserSearch;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User create(UserCreate userCreate);

    UserResponse find(UUID userId);

    List<User> search(UserSearch userSearch);

    List<UserResponse> findAll();

    String update(UserUpdate userUpdate);

    boolean delete(UUID id);

    void printRemainUsers();

    }


