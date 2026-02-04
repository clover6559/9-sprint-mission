package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.UserStatus.UserStatusCreate;
import com.sprint.mission.discodeit.dto.UserStatus.UserStatusUpdate;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;

import java.util.List;
import java.util.UUID;

public class JCFUserStatusService implements UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;


    public JCFUserStatusService(UserStatusRepository userStatusRepository, UserRepository userRepository) {
        this.userStatusRepository = userStatusRepository;
        this.userRepository = userRepository;
    }


    @Override
    public UserStatus create(UserStatusCreate create) {
        userRepository.findById(create.userId())
                .orElseThrow(() -> new RuntimeException("해당 유저를을 찾을 수 없습니다. "));
        if (userStatusRepository.existsByUserId(create.userId())) {
            throw new RuntimeException("해당 유저에 대한 유저 상태가 이미 존재합니다.");
        }
        UserStatus userStatus = new UserStatus(create.userId(), create.statusMessage(), create.statusType());
        return userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus findById(UUID id) {
        return userStatusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 유저 상태를 찾을 수 없습니다."));

    }

    @Override
    public List<UserStatus> findAll() {
        return userStatusRepository.findAll();
    }

    @Override
    public UserStatus updateByUserId(UUID userId, UserStatusUpdate update) {
        UserStatus userStatus =  userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("해당 유저의 상태를 찾을 수 없습니다. "));
        userStatus.updateUserStatus(update.statusMessage(), update.statusType());
        return userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus update(UserStatusUpdate update) {
        UserStatus userStatus = userStatusRepository.findById(update.id())
                .orElseThrow(() -> new RuntimeException("해당 유저 상태를 찾을 수 없습니다."));
        userStatus.updateUserStatus(update.statusMessage(), update.statusType());
        return userStatusRepository.save(userStatus);
    }

    @Override
    public boolean deleteById(UUID id) {
        userStatusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 유저 상태를 찾을 수 없습니다."));
        userStatusRepository.deleteById(id);
        return true;
    }
}
