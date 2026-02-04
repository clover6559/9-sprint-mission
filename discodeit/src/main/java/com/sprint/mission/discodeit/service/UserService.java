package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.UserCreate;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.dto.user.UserUpdate;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.search.UserSearch;

import java.util.List;
import java.util.UUID;

public interface UserService {

    //생성
    User create(UserCreate userCreate);

    //조회(Id)
    UserResponse findById(UUID userId);

    //다건 조회
    List<User> search(UserSearch userSearch);

    // 전체조회
    List<UserResponse> findAll();

    //수정
    String update(UserUpdate userUpdate);

    //삭제
    boolean delete(UUID id);

    void printRemainUsers();

    User findByName(String userName);
    User findByEmail(String email);
    }


