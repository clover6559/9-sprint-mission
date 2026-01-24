package basic;

import entity.User;
import repository.UserRepository;
import service.UserService;
import service.serch.UserSearch;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class BasicUserService implements UserService {
    private final UserRepository userRepository;

public BasicUserService(UserRepository userRepository) {
    this.userRepository = userRepository;
}

    @Override
    public User create(String userName, String email, String password) {
        User user = new User(userName, email, password);
        return userRepository.save(user);
    }

    @Override
    public User findById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + "not found"));
    }

    @Override
    public List<User> Search(UserSearch userSearch) {
        return List.of();
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public String update(UUID userId, String userName, String email, String password) {
       User user =  userRepository.findById(userId)
               .orElseThrow(() -> new NoSuchElementException("User with id " + userId + "not found"));
        user.update(userName,email, password);
        return userRepository.save(user).update(userName,email,password);
    }

    @Override
    public boolean delete(UUID userId) {
    if (!userRepository.existsById(userId)) {
        throw new NoSuchElementException("User with id " + userId + "not found");
    }
        userRepository.deleteById(userId);
        return true;
    }
}