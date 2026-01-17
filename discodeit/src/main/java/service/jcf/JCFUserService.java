package service.jcf;

import entity.User;
import service.UserService;
import service.serch.UserSearch;

import java.util.*;
import java.util.stream.Collectors;

public class JCFUserService implements UserService {
    private final Map<UUID, User> userData = new HashMap<>();

    public JCFUserService() {
    }

//    List<User> userTestData = List.of(""김사연","sayeon@gmail.com", "125rtf")
//    User user1 = new JCFUserService().createUser("김사연","sayeon@gmail.com", "125rtf");

//    User user2 = createUser("김사연","sayeon@gmail.com", "125rtf");

    @Override
    public User createUser(String userName, String email, String password) {
        User user = new User(userName, email, password);
        userData.put(user.getUserId(), user);
        return user;
    }

    @Override
    public User findUserById(UUID userId) {
        return userData.get(userId);
    }

    @Override
    public List<User> findAllUser() {
        return userData.values().stream()
                .toList();
        }

    @Override
    public List<User> UserSearch(UserSearch userSearch) {
        return userData.values().stream()
                .filter(u-> {
                    String searchName = userSearch.getUserName();
                    if (searchName == null) return true;
                    return searchName.equals(u.getUserName());
                })
                .filter(u -> {
                    String searchEmail = userSearch.getEmail();
                    if (searchEmail == null) return true;
                    return searchEmail.equals(u.getEmail());
                })
                .collect(Collectors.toList());
    }

    @Override
    public User updateUser(UUID userId, String userName, String email, String password) {
        User foundUser = userData.get(userId);
        if (foundUser == null) {
            throw new IllegalArgumentException("존재하지 않는 사용자 아이디입니다 : " + userId);
        }
        foundUser.update(userName,email, password);
        userData.put(userId, foundUser);
        return foundUser;
    }

    @Override
    public boolean deleteUser(UUID userId) {
        if (userData.get(userId) == null) {
            System.out.println("실패 : 존재하지 않는 유저 Id 입니다");
            return false;
        }
        userData.remove(userId);
        System.out.println("성공: 유저가 삭제되었습니다.");
        return true;
    }
}
