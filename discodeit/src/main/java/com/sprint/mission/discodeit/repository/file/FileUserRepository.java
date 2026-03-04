package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileUserRepository implements UserRepository {

  private final Path DIRECTORY;
  private final String EXTENSION = ".ser";
  private final FileLockProvider fileLockProvider;


  public FileUserRepository(
      @Value("${discodeit.repository.file-directory:file-data-map}") String fileDirectory,
      FileLockProvider fileLockProvider
  ) {
    System.out.println("[시스템] File 모드가 활성화되었습니다. 저장 경로: " + fileDirectory);
    this.DIRECTORY = Paths.get(System.getProperty("user.dir"), fileDirectory,
        User.class.getSimpleName());
    if (Files.notExists(DIRECTORY)) {
      try {
        Files.createDirectories(DIRECTORY);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    this.fileLockProvider = fileLockProvider;
  }

  private Path resolvePath(UUID id) {
    return DIRECTORY.resolve(id + EXTENSION);
  }


  @Override
  public User save(User user) {
    Path path = resolvePath(user.getId());
    ReentrantLock lock = fileLockProvider.getLock(path);
    lock.lock();
    try (
        FileOutputStream fos = new FileOutputStream(path.toFile());
        ObjectOutputStream oos = new ObjectOutputStream(fos)
    ) {
      oos.writeObject(user);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      lock.unlock();
    }

    return user;
  }

  @Override
  public Optional<User> findById(UUID userId) {
    User userNullable = null;
    Path path = resolvePath(userId);
    ReentrantLock lock = fileLockProvider.getLock(path);
    lock.lock();
    if (Files.exists(path)) {
      try (
          FileInputStream fis = new FileInputStream(path.toFile());
          ObjectInputStream ois = new ObjectInputStream(fis)
      ) {
        userNullable = (User) ois.readObject();
      } catch (IOException | ClassNotFoundException e) {
        throw new RuntimeException(e);
      } finally {
        lock.unlock();
      }
    }
    return Optional.ofNullable(userNullable);
  }

  @Override
  public Optional<User> findByUsername(String username) {
    return this.findAll().stream()
        .filter(user -> user.getUsername().equals(username))
        .findFirst();
  }

  @Override
  public List<User> findAll() {
    try (Stream<Path> paths = Files.list(DIRECTORY)) {
      return paths
          .filter(path -> path.toString().endsWith(EXTENSION))
          .map(path -> {
            ReentrantLock lock = fileLockProvider.getLock(path);
            lock.lock();
            try (
                FileInputStream fis = new FileInputStream(path.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis)
            ) {
              return (User) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
              throw new RuntimeException(e);
            } finally {
              lock.unlock();
            }
          })
          .toList();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean existsById(UUID id) {
    return Files.exists(resolvePath(id));
  }

  @Override
  public void deleteById(UUID id) {
    Path path = resolvePath(id);
    try {
      Files.delete(path);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean existsByUsername(String username) {
    return this.findAll().stream()
        .anyMatch(user -> user.getUsername().equals(username));
  }


  @Override
  public boolean existsByEmail(String email) {
    return this.findAll().stream()
        .anyMatch(user -> user.getEmail().equals(email));
  }
}
