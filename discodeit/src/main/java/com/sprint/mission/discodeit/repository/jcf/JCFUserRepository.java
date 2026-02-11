package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;
@Repository
@ConditionalOnProperty(name = "discodeit.repository.type",havingValue = "jcf", matchIfMissing = true)
public class JCFUserRepository implements UserRepository {
        private final Map<UUID, User> userRepo = new HashMap<>();

        public JCFUserRepository() {
            System.out.println("[시스템] JCF 모드가 활성화되었습니다. 데이터를 메모리에 저장합니다.");
        }
        @Override
        public User save(User user) {
            userRepo.put(user.getId(), user);
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
                .filter(user -> userName != null &&  userName.equals(user.getUserName()))
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