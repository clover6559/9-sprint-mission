package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.user.UserCreate;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.dto.user.UserUpdate;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.search.UserSearch;
import org.springframework.stereotype.Repository;

import java.util.*;


@Repository
public class JCFUserService implements UserService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;


    public JCFUserService(UserRepository userRepository, UserStatusRepository userStatusRepository, BinaryContentRepository binaryContentRepository) {
        this.userRepository = userRepository;
        this.userStatusRepository = userStatusRepository;
        this.binaryContentRepository = binaryContentRepository;
    }

    @Override
    public User create(UserCreate UserCreate) {
        if (userRepository.findByName(UserCreate.basicUserInfo().userName()) != null) {
            throw new RuntimeException("이미 존재하는 이름입니다.");
        }
        if (userRepository.findByEmail(UserCreate.basicUserInfo().email()) != null) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }
        User savedUser = new User(UserCreate);
        userRepository.save(savedUser);

        if (UserCreate.profileImageInfo() != null) {
            BinaryContent profileImage = new BinaryContent(savedUser.getUserId(), UserCreate.profileImageInfo().fileName(), UserCreate.profileImageInfo().data());
            binaryContentRepository.save(profileImage);
        }

        UserStatus userStatus = new UserStatus(UserStatus.Status.ONLINE, savedUser.getUserId());
        userStatusRepository.save(userStatus);
        return savedUser;

    }

    @Override
    public UserResponse findById(UUID userId) {
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수 없습니다."));
        UserStatus userStatus = userStatusRepository.findByUserId(findUser.getUserId());
        boolean isOnline = userStatus.isOnline();
        return new UserResponse(isOnline, findUser.getUserId(), findUser.getUserName(), findUser.getEmail());

    }

    @Override
    public List<UserResponse> findAll() {
        List<User> userList = userRepository.findAll();
        return userList.stream()
                .map(user -> {
                    UserStatus status = userStatusRepository.findByUserId(user.getUserId());
                    boolean isOnline = (status != null) && status.isOnline();
                    return new UserResponse(
                            isOnline, user.getUserId(), user.getUserName(), user.getEmail()
                    );
                })
                .toList();
    }

    @Override
    public List<User> search(UserSearch userSearch) {
        return userRepository.findAll().stream()
                .filter(u -> userSearch.getUserName() == null || userSearch.getUserName().equals(u.getUserName()))
                .toList();
    }

    @Override
    public String update(UserUpdate UserUpdate) {
        User findUser = userRepository.findById(UserUpdate.targetId())
                .orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수 없습니다."));
        String changes = findUser.changes(UserUpdate.userUpdateInfo());
        if (UserUpdate.userUpdateInfo().profileImageInfo() != null) {
            BinaryContent newContent = new BinaryContent(
                    findUser.getUserId(),
                    UserUpdate.userUpdateInfo().profileImageInfo().fileName(),
                    UserUpdate.userUpdateInfo().profileImageInfo().data()
            );
            binaryContentRepository.save(newContent);
            UUID oldProfileId = findUser.getProfileId();
            findUser.updateProfileId(newContent.getId());
            if (oldProfileId != null) {
                binaryContentRepository.deleteById(oldProfileId);
            }
            changes += ", 프로필 이미지 교체됨";
        }
        userRepository.save(findUser);
        return changes + " 및 프로필 이미지 업데이트 완료";
    }


    @Override
    public boolean delete(UUID userId) {
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수 없습니다."));
        userStatusRepository.deleteByUserId(userId);
        if (findUser.getProfileId() != null) {
            binaryContentRepository.deleteById(findUser.getProfileId());
        }
        userRepository.deleteById(userId);
        return true;
    }

    public void printRemainUsers() {
        List<User> users = userRepository.findAll();
        System.out.println("현재 남은 유저 수: " + users.size());
        users.forEach(u -> System.out.println("- " + u.getUserName()));
    }

    @Override
    public User findByName(String userName) {
        return userRepository.findByName(userName);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
