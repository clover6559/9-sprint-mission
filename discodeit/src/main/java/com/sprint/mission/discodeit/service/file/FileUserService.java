package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.dto.user.UserCreate;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.dto.user.UserUpdate;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.search.UserSearch;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileUserService implements UserService {
    private final Path DIRECTORY;
    private final String EXTENSION = ".ser";

    public FileUserService() {
        this.DIRECTORY = Paths.get(System.getProperty("user.dir"), "file-data-map", User.class.getSimpleName());
        if (Files.notExists(DIRECTORY)) {
            try {
                Files.createDirectories(DIRECTORY);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Path resolvePath(UUID id) {
        return DIRECTORY.resolve(id + EXTENSION);
    }

    @Override
    public User create(UserCreate UserCreate) {
        User user = new User(UserCreate);
        Path path = resolvePath(user.getUserId());
        try (
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return user;
    }

    @Override
    public UserResponse findById(UUID userId) {
        User userNullable = null;
        Path path = resolvePath(userId);
        if (Files.exists(path)) {
            try (
                    FileInputStream fis = new FileInputStream(path.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fis)
            ) {
                userNullable = (User) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        return Optional.ofNullable(userNullable)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
    }

    @Override
    public List<User> Search(UserSearch userSearch) {
        List<User> users = new ArrayList<>();
        try {
            DirectoryStream<Path> paths = Files.newDirectoryStream(DIRECTORY, "*" + EXTENSION);
            for (Path path : paths) {
                User userNullable = null;
                if (Files.exists(path)) {
                    try (
                            FileInputStream fis = new FileInputStream(path.toFile());
                            ObjectInputStream ois = new ObjectInputStream(fis)
                    ) {
                        userNullable = (User) ois.readObject();
                    }
                }
                if (userNullable != null && userSearch.matches(userNullable)) {
                    users.add(userNullable);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    @Override
    public List<UserResponse> findAll() {
        try {
            return Files.list(DIRECTORY)
                    .filter(path -> path.toString().endsWith(EXTENSION))
                    .map(path -> {
                        try (
                                FileInputStream fis = new FileInputStream(path.toFile());
                                ObjectInputStream ois = new ObjectInputStream(fis)
                        ) {
                            return (User) ois.readObject();
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String update(UserUpdate UserUpdate) {
        User userNullable = null;
        Path path = resolvePath(UserUpdate.targetId());
        if (Files.exists(path)) {
            try (
                    FileInputStream fis = new FileInputStream(path.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fis)
            ) {
                userNullable = (User) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        User user = Optional.ofNullable(userNullable)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
        user.changes(UserUpdate.userUpdateInfo());
        try (
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return user.changes(UserUpdate.userUpdateInfo());
    }

    @Override
    public boolean delete(UUID userId) {
        Path path = resolvePath(userId);
        if (Files.notExists(path)) {
            throw new NoSuchElementException("User with id " + userId + " not found");
        }
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public void printRemainUsers() {
        List<User> users = resolvePath.
        if (users.isEmpty()) {
            throw new NoSuchElementException("남아있는 유저가 없습니다.");
        }System.out.println("현재 남은 유저 수: " + users.size());
        users.forEach(u -> System.out.println("- " + u.getUserName()));
        }

    @Override
    public User findByName(String userName) {
        return null;
    }

    @Override
    public User findByEmail(String email) {
        return null;
    }
}

