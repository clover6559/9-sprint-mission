package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.serch.UserSearch;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public interface UserService {
    public record UserCreate(
            String userName, String email, String password, UserStatus status) {}
    public record content(UUID profileId) {}
    public record UserUpdate(
            UUID userId, String userName, String email, String password) {}
    //생성
    content create(UserCreate create);

    //조회(Id)
    User findById(UUID userId);

    //다건 조회
    List<User> Search(UserSearch userSearch);

    // 전체조회
    List<User> findAll();

    //수정
    String update(UserUpdate update);

    //삭제
    boolean delete(UUID id);

    void printRemainUsers();
    }
//public record UserCreate(
//        String userName, String email, String password, UserStatus status) {}
//public record content(UUID profileId) {}
//public record UserUpdate(
//        UUID userId, String userName, String email, String password) {}


