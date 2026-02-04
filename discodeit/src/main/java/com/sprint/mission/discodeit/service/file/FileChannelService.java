package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.search.ChannelSearch;

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
    public ChannelResponse findCById(UUID channelId) {
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
    public List<ChannelResponse> findAllByUserId() {
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
    public String update(UUID channelId, String channelName, String description) {
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

        return channel.update(channelName,description);
    }


    @Override
    public boolean deleteById(UUID channelId) {
        Path path = resolvePath(channelId);
        if (Files.notExists(path)) {
            throw new NoSuchElementException("Channel with id " + channelId + " not found");
        }
        try {
            Files.deleteById(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public void printRemainChannel() {
        List<Channel> channels = findAllByUserId();
        if (channels.isEmpty()) {
            throw new NoSuchElementException("남아있는 채널이 없습니다.");
        }System.out.println("현재 남은 채널 수: " + channels.size());
        channels.forEach(c -> System.out.println("- " + c.getChannelName()));
    }
        }