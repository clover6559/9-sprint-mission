package service.jcf;

import entity.User;
import service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private final Map<UUID, User> userData = new HashMap<>();

    @Override
    public User addUser(String displayName, String phoneNumber, String email) {
        User user = new User(displayName, phoneNumber, email);
        userData.put(user.getUserId(), user);
        return user;
    }

    @Override
    public User findUser(UUID id) {
        return userData.get(id);
    }

    @Override
    public List<User> getAllUser() {
        return userData.values().stream().toList();
    }

    @Override
    public void updateUser(User user) {
        User foundUser = userData.get(user.getUserId());
        if (foundUser == null) {
            return;
        }
        foundUser.updateUser(user.getDisplayName(), user.getEmail(), user.getPhoneNumber());
            userData.put(user.getUserId(), foundUser);
        }

    @Override
    public boolean deleteUser(UUID id) {
        if (userData.get(id) == null) {
            return false;
        }
        userData.remove(id);
        return true;
    }
}
