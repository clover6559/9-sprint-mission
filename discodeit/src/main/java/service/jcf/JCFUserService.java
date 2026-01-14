package service.jcf;

import entity.User;
import service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private final Map<UUID, User> Data = new HashMap<>();

    @Override
    public User create(String userName, String email, String password) {
        User user = new User(userName, email, password);
        Data.put(user.getUserId(), user);
        return user;
    }

    @Override
    public User find(UUID userId) {
        return Data.get(userId);
    }

    @Override
    public List<User> findAll() {
        return Data.values().stream().toList();
    }

    @Override
    public User update(UUID userId, String userName, String email, String password) {
        User foundUser = Data.get(userId);
        if (foundUser == null) {
            throw new IllegalArgumentException("존재하지 않는 사용자 아이디입니다 : " + userId);
        }
        foundUser.update(userName,email, password);
        Data.put(userId, foundUser);
        System.out.println("성공: 사용자 정보가 업데이트되었습니다.");
        return foundUser;
    }

    @Override
    public boolean delete(UUID userId) {
        if (Data.get(userId) == null) {
            return false;
        }
        Data.remove(userId);
        return true;
    }
}
