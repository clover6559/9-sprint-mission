package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserCreate;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.search.UserSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;

    @Override
    public User create(UserCreate userCreate) {
        User user = new User(userCreate);
        return userRepository.save(user);
    }

    @Override
    public UserResponse findById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + userId));
        return new UserResponse(false, user.getUserId(), user.getUserName(), user.getEmail());
    }

    @Override
    public List<User> Search(UserSearch userSearch) {
        return userRepository.findAll().stream()
                .filter(u -> userSearch.getUserName() == null || userSearch.getUserName().equals(u.getUserName()))
                .toList();
    }

    @Override
    public List<UserResponse> findAll() {
        return userRepository.findAll();
    }

    @Override
    public String update(UUID userId, String userName, String email, String password) {
       User user =  userRepository.findById(userId)
               .orElseThrow(() -> new NoSuchElementException("User with id " + userId + "not found"));
        user.changes(userName,email, password);
        userRepository.save(user);
        return user.toString();
    }

    @Override
    public boolean delete(UUID userId) {
    if (!userRepository.existsById(userId)) {
        throw new NoSuchElementException("User with id " + userId + "not found");
    }
        userRepository.deleteById(userId);
        return true;
    }

    @Override
    public void printRemainUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            System.out.println("\n 남아있는 유저 정보가 존재하지 않습니다.");
        } else {
            System.out.println("현재 남은 유저 수: " + users.size());
            users.forEach(u -> System.out.println("- " + u.getUserName()));
        }
    }
