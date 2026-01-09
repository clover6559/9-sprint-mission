package service.jcf;

import entity.User;
import service.UserService;

import java.util.List;

public class JCFUserService implements UserService {
    private


    @Override
    public User addUser(User user) {
        return null;
    }

    @Override
    public User getUser(String displayName) {
        return null;
    }

    @Override
    public List<User> getAllUser() {
        return List.of();
    }

    @Override
    public User updateUser(String displayNmae, String email, String phoneNumber) {
        return null;
    }

    @Override
    public boolean delectUser(String displayName) {
        return false;
    }
}
