package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserCreate;
import com.sprint.mission.discodeit.dto.user.UserFind;
import com.sprint.mission.discodeit.dto.user.UserUpdate;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.search.UserSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;


    @Override
    public User create(UserCreate userCreate) {
        if (userRepository.findByName(userCreate.basicUserInfo().userName()) != null) {
            throw new RuntimeException("이미 존재하는 이름입니다.");
        }
        if (userRepository.findByEmail(userCreate.basicUserInfo().email()) != null) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }
        User savedUser = new User(userCreate);
        userRepository.save(savedUser);

        if (userCreate.profileImageInfo() != null) {
            BinaryContent profileImage = new BinaryContent(savedUser.getId(), userCreate.profileImageInfo().fileName(), userCreate.profileImageInfo().data());
            binaryContentRepository.save(profileImage);
        }

        UserStatus userStatus = new UserStatus(savedUser.getId(), "기본 상태 메시지", UserStatus.Status.ONLINE);
        userStatusRepository.save(userStatus);
        return savedUser;

    }

    @Override
    public UserFind find(UUID userId) {
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수 없습니다."));
        UserStatus userStatus = userStatusRepository.findByUserId(findUser.getId())
                .orElse(null);
        boolean isOnline = (userStatus != null) && userStatus.isOnline();
        return new UserFind(isOnline, findUser.getId(), findUser.getUserName(), findUser.getEmail());

    }

    @Override
    public List<UserFind> findAll() {
        List<User> userList = userRepository.findAll();
        return userList.stream()
                .map(user -> {
                    UserStatus status = userStatusRepository.findByUserId(user.getId())
                            .orElse(null);
                    boolean isOnline = (status != null) && status.isOnline();
                    return new UserFind(
                            isOnline, user.getId(), user.getUserName(), user.getEmail()
                    );
                })
                .toList();
    }

    @Override
    public List<UserFind> search(UserSearch userSearch) {
        return userRepository.findAll().stream()
                .filter(u -> userSearch.getUserName() == null || userSearch.getUserName().equals(u.getUserName()))
                .map(user -> {
                   UserStatus status =  userStatusRepository.findByUserId(user.getId())
                           .orElse(null);
                   boolean isOnline = (status != null) && status.isOnline();
                   return new UserFind(
                           isOnline, user.getId(), user.getUserName(), user.getEmail()
                   );
                })
                .toList();
    }

    @Override
    public void update(UserUpdate userUpdate) {
        User findUser = userRepository.findById(userUpdate.targetId())
                .orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수 없습니다."));
        findUser.updateInfo(userUpdate.userUpdateInfo());
        if (userUpdate.userUpdateInfo().profileImageInfo() != null) {
            BinaryContent newContent = new BinaryContent(
                    findUser.getId(),
                    userUpdate.userUpdateInfo().profileImageInfo().fileName(),
                    userUpdate.userUpdateInfo().profileImageInfo().data()
            );
            binaryContentRepository.save(newContent);
            UUID oldProfileId = findUser.getProfileId();
            findUser.updateProfileId(newContent.getId());
            if (oldProfileId != null) {
                binaryContentRepository.deleteById(oldProfileId);
            }
            System.out.println("[Service]" +'\n' + "프로필 이미지 교체 완료");
        }
        userRepository.save(findUser);
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

}
