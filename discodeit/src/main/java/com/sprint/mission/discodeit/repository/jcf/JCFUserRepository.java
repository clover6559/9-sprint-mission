package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

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
        public boolean existsById(UUID userId) {
            return userRepo.containsKey(userId);
        }


        @Override
        public void deleteById(UUID userId) {
             userRepo.remove(userId);
}

    @Override
    public User findByName(String userName) {
        return userRepo.values().stream()
                .filter(user -> user.getUserName().equals(userName))
                .findFirst()
                .orElse(null);
    }

    @Override
    public User findByEmail(String email) {
        return userRepo.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

}