package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.serch.UserSearch;

import java.util.*;

public class JCFUserService implements UserService {
    private final Map<UUID, User> userData = new HashMap<>();

    public JCFUserService() {
        saveDummyUser("김사연","sayeon@gmail.com", "125rtf");
        saveDummyUser("강지원", "jiwon@gmail.com", "fgd123");
    }
    private void saveDummyUser(String name, String email, String password) {
        User user = new User(name, email, password);
        userData.put(user.getUserId(), user);
    }

    @Override
    public User create(String userName, String email, String password) {
        User user = new User(userName, email, password);
        userData.put(user.getUserId(), user);
        return user;
    }

    @Override
    public User findById(UUID userId) {
        return userData.get(userId);
    }

    @Override
    public List<User> findAll() {
        return userData.values().stream().toList();
        }

    @Override
    public List<User> Search(UserSearch userSearch) {
        return userData.values().stream()
                .filter(u-> userSearch.getUserName() == null || userSearch.getUserName().equals(u.getUserName()))
                .toList();
    }

    @Override
    public String update(UUID userId, String newUserName, String newEmail, String newPassword) {
        User foundUser = userData.get(userId);
        if (foundUser == null) {
            throw new IllegalArgumentException("존재하지 않는 사용자 아이디입니다 : " + userId);
        }
        return foundUser.update(newUserName, newEmail, newPassword);
    }

    @Override
    public boolean delete(UUID userId) {
        if (userData.get(userId) == null) {
            System.out.println("실패 : 존재하지 않는 유저 Id 입니다");
            return false;
        }
        userData.remove(userId);
        return true;
    }

    public void printRemainUsers() {
        List<User> users = findAll();
        System.out.println("현재 남은 유저 수: " + users.size());
        users.forEach(u -> System.out.println("- " + u.getUserName()));
    }
}
