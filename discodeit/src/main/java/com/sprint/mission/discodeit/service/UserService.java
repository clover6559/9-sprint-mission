package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.serch.UserSearch;

import java.util.List;
import java.util.UUID;

public interface UserService {
    //생성
    User create(String userName, String email, String password);

    //조회(Id)
    User findById(UUID userId);

    //다건 조회
    List<User> Search(UserSearch userSearch);

    // 전체조회
    List<User> findAll();

    //수정
    String update(UUID userId, String userName, String email, String password);

    //삭제
    boolean delete(UUID id);

    void printRemainUsers();
    }



