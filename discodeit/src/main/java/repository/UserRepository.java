package repository;

import entity.User;
import service.serch.UserSearch;

import java.util.List;
import java.util.UUID;

public interface UserRepository {
    User save(User user);

    User findById(UUID userId);

    List<User> userSearch(UserSearch userSearch);

    List<User> findAll();

    User updateUser(UUID userId, String newUsername, String newEmail, String newPassword);

    boolean deleteById(UUID userId);
}
