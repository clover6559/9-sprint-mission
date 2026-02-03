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
        return null;
    }

    @Override
    public void logout(UUID userId) {

    }
}

