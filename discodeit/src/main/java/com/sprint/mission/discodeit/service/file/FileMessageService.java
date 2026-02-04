//package com.sprint.mission.discodeit.service.file;
//
//import com.sprint.mission.discodeit.entity.Channel;
//import com.sprint.mission.discodeit.entity.Message;
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.service.ChannelService;
//import com.sprint.mission.discodeit.service.MessageService;
//import com.sprint.mission.discodeit.service.UserService;
//import com.sprint.mission.discodeit.service.search.MessageSearch;
//
//import java.io.*;
//import java.nio.file.DirectoryStream;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.*;
//
//public class FileMessageService implements MessageService {
//    private final Path DIRECTORY;
//    private final String EXTENSION = ".ser";
//
//    public FileMessageService(UserService userService, ChannelService channelService) {
//        this.DIRECTORY = Paths.get(System.getProperty("user.dir"), "file-data-map", Message.class.getSimpleName());
//        if (Files.notExists(DIRECTORY)) {
//            try {
//                Files.createDirectories(DIRECTORY);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }
//
//    private Path resolvePath(UUID id) {
//        return DIRECTORY.resolve(id + EXTENSION);
//    }
//
//    @Override
//    public Message create(String content, User user, Channel channel) {
//        Message message = new Message(content, user, channel);
//        Path path = resolvePath(message.getId());
//        try (
//                FileOutputStream fos = new FileOutputStream(path.toFile());
//                ObjectOutputStream oos = new ObjectOutputStream(fos)
//        ) {
//            oos.writeObject(message);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return message;
//    }
//
//    @Override
//    public Message findById(UUID messageId) {
//        Message messageNullable = null;
//        Path path = resolvePath(messageId);
//        if (Files.exists(path)) {
//            try (
//                    FileInputStream fis = new FileInputStream(path.toFile());
//                    ObjectInputStream ois = new ObjectInputStream(fis)
//            ) {
//                messageNullable = (Message) ois.readObject();
//            } catch (IOException | ClassNotFoundException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//        return Optional.ofNullable(messageNullable)
//                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + " not found"));
//    }
//
//
//    @Override
//    public List<Message> Search(MessageSearch messageSearch) {
//        List<Message> messages = new ArrayList<>();
//        try {
//            DirectoryStream<Path> paths = Files.newDirectoryStream(DIRECTORY, "*" + EXTENSION);
//            for (Path path : paths) {
//                Message messageNullable = null;
//                if (Files.exists(path)) {
//                    try (
//                            FileInputStream fis = new FileInputStream(path.toFile());
//                            ObjectInputStream ois = new ObjectInputStream(fis)
//                    ) {
//                        messageNullable = (Message) ois.readObject();
//                    }
//                }
//
//                if (messageNullable != null) {
//                    boolean match = true;
//                    if (messageSearch.getChannelName() != null) {
//                        match = messageNullable
//                                .getChannelName()
//                                .contains(messageSearch.getChannelName());
//                    }
//
//                    if (match && messageSearch.getUserName() != null) {
//                        match = messageNullable
//                                .getUserName()
//                                .contains(messageSearch.getUserName());
//                    }
//                    if (match) {
//                        messages.add(messageNullable);
//                    }
//                }
//            }
//        } catch (IOException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//        return messages;
//    }
//
//
//    @Override
//    public List<Message> findAllByChannelId() {
//        try {
//            return Files.list(DIRECTORY)
//                    .filter(path -> path.toString().endsWith(EXTENSION))
//                    .map(path -> {
//                        try (
//                                FileInputStream fis = new FileInputStream(path.toFile());
//                                ObjectInputStream ois = new ObjectInputStream(fis)
//                        ) {
//                            return (Message) ois.readObject();
//                        } catch (IOException | ClassNotFoundException e) {
//                            throw new RuntimeException(e);
//                        }
//                    })
//                    .toList();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//
//    @Override
//    public String update(UUID messageId, String content) {
//        Message messageNullable = null;
//        Path path = resolvePath(messageId);
//        if (Files.exists(path)) {
//            try (
//                    FileInputStream fis = new FileInputStream(path.toFile());
//                    ObjectInputStream ois = new ObjectInputStream(fis)
//            ) {
//                messageNullable = (Message) ois.readObject();
//            } catch (IOException | ClassNotFoundException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//        Message message = Optional.ofNullable(messageNullable)
//                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + " not found"));
//        message.update(content);
//
//        try(
//                FileOutputStream fos = new FileOutputStream(path.toFile());
//                ObjectOutputStream oos = new ObjectOutputStream(fos)
//        ) {
//            oos.writeObject(message);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return message.update(content);
//    }
//
//
//    @Override
//    public boolean deleteById(UUID messageId) {
//        Path path = resolvePath(messageId);
//        if (Files.notExists(path)) {
//            throw new NoSuchElementException("MassageId with id " + messageId + " not found");
//        }
//        try {
//            Files.deleteById(path);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return false;
//    }
//
//    @Override
//    public void printRemainMessages() {
//        List<Message> messages = findAllByChannelId();
//        if (messages.isEmpty()) {
//            throw new NoSuchElementException("남아있는 메시지가 없습니다.");
//        }System.out.println("현재 남은 메세지 수: " + messages.size());
//        messages.forEach(m -> System.out.println("- " + m.getContent()));
//    }
//}
