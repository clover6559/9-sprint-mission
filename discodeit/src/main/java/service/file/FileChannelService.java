package service.file;

import entity.Channel;
import entity.User;
import service.ChannelService;
import service.UserService;
import service.serch.ChannelSearch;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

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
    public Channel create(Channel.ChannelType channelType, String channelName, String description, User user) {
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
    public Channel findCById(UUID channelId) {
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

    @Override
    public List<Channel> Search(ChannelSearch channelSearch) {
        List<Channel> channels = new ArrayList<>();

        try {
            DirectoryStream<Path> paths = Files.newDirectoryStream(DIRECTORY, "*" + EXTENSION);

            for (Path path : paths) {
                Channel channelNullable = null;
                if (Files.exists(path)) {
                    try (
                            FileInputStream fis = new FileInputStream(path.toFile());
                            ObjectInputStream ois = new ObjectInputStream(fis)
                    ) {
                        channelNullable = (Channel) ois.readObject();
                    }
                }

                if (channelNullable != null) {
                    boolean match = true;
                    if (channelSearch.getChannelName() != null) {
                        match = channelNullable.getChannelName()
                                .contains(channelSearch.getChannelName());
                    }
                    if (match && channelSearch.getUserName() != null) {
                        match = channelNullable.getUserName()
                                .contains(channelSearch.getUserName());
                    }
                    if (match) {
                        channels.add(channelNullable);
                    }
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return channels;
    }



    @Override
    public List<Channel> findAll() {
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
    public Channel update(UUID channelId, String channelName, String description) {
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
    public boolean delete(UUID channelId) {
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

    @Override
    public void printRemainChannel() {
        List<Channel> channels = findAll();
        if (channels.isEmpty()) {
            System.out.println("남아있는 채널이 없습니다.");
            return;
        }
        System.out.println("=== 남아있는 채널 목록 ===");

        for (Channel channel : channels) {
            System.out.println(
                    "ID: " + channel.getChannelId() +
                            ", 채널명: " + channel.getChannelName() +
                            ", 설명: " + channel.getDescription() +
                            ", 생성자: " + channel.getUserName()
            );
        }
    }

}
