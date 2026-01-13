package service;

import entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    //생성
    User create(String userName, String email, String password);

    //조회
    User find(UUID userId);

    //  전체조회
    List<User> findAll();
    //수정
    User update(UUID userId, String userName, String email, String password);

    //삭제
    boolean delete(UUID id);


}
