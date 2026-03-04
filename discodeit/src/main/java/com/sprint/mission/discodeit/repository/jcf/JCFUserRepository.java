package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
public class JCFUserRepository implements UserRepository {

  private final Map<UUID, User> data;

  public JCFUserRepository() {
    this.data = new HashMap<>();
    {
      System.out.println("[시스템] JCF 모드가 활성화되었습니다. 데이터를 메모리에 저장합니다.");
    }
  }

  @Override
  public User save(User user) {
    this.data.put(user.getId(), user);
    return user;
  }

  @Override
  public Optional<User> findById(UUID id) {
    return Optional.ofNullable(this.data.get(id));
  }

  @Override
  public Optional<User> findByUsername(String username) {
    return this.findAll().stream()
        .filter(user -> user.getUsername().equals(username))
        .findFirst();
  }

  @Override
  public List<User> findAll() {
    return this.data.values().stream().toList();
  }

  @Override
  public boolean existsById(UUID id) {
    return this.data.containsKey(id);
  }

  @Override
  public void deleteById(UUID id) {
    this.data.remove(id);
  }

  @Override
  public boolean existsByUsername(String username) {
    return this.findAll().stream().anyMatch(user -> user.getUsername().equals(username));
  }

  @Override
  public boolean existsByEmail(String email) {
    return this.findAll().stream().anyMatch(user -> user.getEmail().equals(email));
  }

}