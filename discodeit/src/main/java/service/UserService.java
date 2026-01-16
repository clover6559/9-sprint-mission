package service;

import entity.User;
import service.serch.UserSearch;

import java.util.List;
import java.util.UUID;

public interface UserService {
    //생성
    User createUser(String userName, String email, String password);

    //조회(Id)
    User findUserById(UUID userId);

    //다건 조회
    List<User> UserSearch(UserSearch userSearch);

    // 전체조회
    List<User> findAllUser();

    //수정
    User updateUser(UUID userId, String userName, String email, String password);

    //삭제
    boolean deleteUser(UUID id);


}
