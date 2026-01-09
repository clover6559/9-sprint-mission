package service;

import entity.User;

import java.util.List;

public interface UserService {
    //생성
    User addUser(User user);

    //조회
    User getUser(String displayName);

    //  전체조회
    List<User> getAllUser();

//    ??수정
    User updateUser(String displayNmae, String email, String phoneNumber);

    //삭제
    boolean delectUser(String displayName);

}
