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
        User user = userRepository.findByName(autowired.userName());
        if (user != null && user.getPassword().equals(autowired.password())) {
            return user;
        }
        throw new RuntimeException("아이디 또는 비밀번호가 일치하지 않습니다.");
    }

    @Override
    public void logout(UUID userId) {

    }
}

