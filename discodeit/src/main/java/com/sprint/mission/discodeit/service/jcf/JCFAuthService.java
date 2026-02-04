//package com.sprint.mission.discodeit.service.jcf;
//
//import com.sprint.mission.discodeit.dto.loginDto;
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.repository.UserRepository;
//import com.sprint.mission.discodeit.service.AuthService;
//import org.springframework.stereotype.Service;
//
//import java.util.UUID;
//
//@Service
//public class JCFAuthService implements AuthService {
//    private final UserRepository userRepository;
//
//    public JCFAuthService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    public User login(loginDto autowired) {
//        User user = userRepository.findByName(autowired.userName());
//        if (user != null && user.getPassword().equals(autowired.password())) {
//            return user;
//        }
//        return null;
//    }
//
//    @Override
//    public void logout(UUID userId) {
//
//    }
//}
