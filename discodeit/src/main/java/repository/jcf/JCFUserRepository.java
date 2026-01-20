package repository.jcf;

import entity.User;
import repository.UserRepository;
import service.serch.UserSearch;

import java.io.*;
import java.util.*;

public class JCFUserRepository implements UserRepository {
        private final Map<UUID, User> userRepo = new HashMap<>();

        public JCFUserRepository() {
        }
        @Override
        public User save(User user) {
            userRepo.put(user.getUserId(), user);
            return user;
        }

        @Override
        public User findById(UUID userId) {
            return userRepo.get(userId);

        }

        @Override
        public List<User> userSearch(UserSearch userSearch) {
            return List.of();
        }

        @Override
        public List<User> findAll() {
            return userRepo.values().stream().toList();
        }

        @Override
        public User updateUser(UUID userId, String newUsername, String newEmail, String newPassword) {
        return userRepo.get(userId);

        }

        @Override
        public boolean deleteById(UUID userId) {
            if (userRepo.get(userId) == null) {
                System.out.println("실패 : 존재하지 않는 유저 Id 입니다");
                return false;
            }
            userRepo.remove(userId);
            return true;}
}

