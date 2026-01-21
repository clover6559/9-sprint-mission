package repository.jcf;

import entity.User;
import repository.UserRepository;
import service.serch.UserSearch;

import java.io.*;
import java.nio.file.Files;
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
        public Optional<User> findById(UUID userId) {
            return Optional.ofNullable(userRepo.get(userId));
        }

        @Override
        public List<User> findAll() {
            return userRepo.values().stream().toList();
        }

        @Override
        public boolean existsById(UUID id) {
            return userRepo.containsKey(id);
        }

        public User updateUser(UUID userId, String newUsername, String newEmail, String newPassword) {
        User user = findById(userId)
                .orElseThrow(() -> new NoSuchElementException("수정할 유저가 없습니다."));
        user.update(newUsername, newEmail, newPassword);
        return user;
        }

        @Override
        public void deleteById(UUID userId) {
            if (userRepo.get(userId) == null) {
                System.out.println("실패 : 존재하지 않는 유저 Id 입니다");
            }
            userRepo.remove(userId);
}

}