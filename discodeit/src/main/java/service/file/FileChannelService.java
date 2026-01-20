package service.file;

import entity.Channel;
import entity.User;
import service.ChannelService;
import service.UserService;
import service.serch.ChannelSearch;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public class FileChannelService implements ChannelService {
    private final Path DIRECTORY;
    private final String EXTENSION = ".ser";

    public FileChannelService(UserService userService) {
        this.DIRECTORY = Paths.get(System.getProperty("user.dir"), "file-data-map", Channel.class.getSimpleName());
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
    public Channel createChannel(Channel.ChannelType channelType, String channelName, String description, User user) {
        Channel channel = new Channel(channelType, channelName, description, user);
        Path path = resolvePath(channel.getChannelId());
        try (
            FileOutputStream fos = new FileOutputStream(path.toFile());
            ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
                oos.writeObject(channel);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        return channel;
    }

    @Override
    public Channel findChannelById(UUID channelId) {
        Channel channelNullable = null;
        Path path = resolvePath(channelId);
        if (Files.exists(path)) {
            try (
                    FileInputStream fis = new FileInputStream(path.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fis)
            ) {
                channelNullable = (Channel) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        return Optional.ofNullable(channelNullable)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));
    }

//TOdo seach부분 구현
    @Override
    public List<Channel> ChannelSearch(ChannelSearch channelSearch) {
        return List.of();
    }

    @Override
    public List<Channel> findAllChannel() {
        try {
            return Files.list(DIRECTORY)
                    .filter(path -> path.toString().endsWith(EXTENSION))
                    .map(path -> {
                        try (
                                FileInputStream fis = new FileInputStream(path.toFile());
                                ObjectInputStream ois = new ObjectInputStream(fis)
                        ) {
                            return (Channel) ois.readObject();
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
    public Channel updateChannel(UUID channelId, String channelName, String description) {
        Channel channelNullable = null;
        Path path = resolvePath(channelId);
        if (Files.exists(path)) {
            try (
                    FileInputStream fis = new FileInputStream(path.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fis)
            ) {
                channelNullable = (Channel) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        Channel channel = Optional.ofNullable(channelNullable)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));
        channel.update(channelName, description);

        try(
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(channel);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return channel;
    }


    @Override
    public boolean deleteChannel(UUID channelId) {
        Path path = resolvePath(channelId);
        if (Files.notExists(path)) {
            throw new NoSuchElementException("Channel with id " + channelId + " not found");
        }
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
