package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.loginDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {
    private final UserRepository userRepository;

    @Override
    public User login(loginDto autowired) {
        String username = autowired.userName();
        String password = autowired.password();
        User user = userRepository.findByName(username);

        if (user != null && !user.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return user;
    }

    @Override
    public void logout(UUID userId) {

    }
}

