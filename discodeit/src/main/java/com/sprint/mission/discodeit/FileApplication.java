package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;


import java.util.List;

public class FileApplication {
        static void userCRUDTest(UserService userService) {
            // 생성
            User user = userService.create("강지원","jinyoong@gmail.com", "566wrsd");
            System.out.println("[유저 생성]" + '\n' +  user.toString());
            // 조회
            User foundUser = userService.findById(user.getUserId());
            System.out.println("[유저 조회(Id)] " + '\n' +  foundUser.toString());
            List<User> foundUsers = userService.findAll();
            System.out.println("[유저 조회(전체)] ");
            foundUsers.forEach(System.out::println);
            //수정
            String updatedUser = userService.update(user.getUserId(), null, null, "woody5678");
            System.out.println("[유저 수정]" +  '\n' + "[변경 사항]" + '\n' +  updatedUser);
            System.out.println();
            System.out.println("[현재 유저 정보] " + '\n' + user);

            // 삭제
            userService.delete(user.getUserId());
            System.out.println("[유저 삭제]");
            userService.printRemainUsers();
            System.out.println();
        }

        static void channelCRUDTest(ChannelService channelService, User user) {
            // 생성
            Channel channel = channelService.create(Channel.ChannelType.PUBLIC, "공지", "공지 채널입니다.", user);
            System.out.println("[채널 생성]" + '\n' + channel.toString());
            // 조회
            Channel foundChannel = channelService.findCById(channel.getChannelId());
            System.out.println("[채널 조회(Id)]" + '\n' + foundChannel.toString());
            List<Channel> foundChannels = channelService.findAllByUserId();
            System.out.println("[채널 전체 조회]");
            foundChannels.forEach(System.out::println);
            // 수정
            String updatedChannel = channelService.update(channel.getChannelId(), "공지사항", null);
            System.out.println("[채널 수정]" + '\n' + "[변경 사항]" + '\n' +  updatedChannel);
            System.out.println();
            System.out.println("[현재 채널 정보] " + '\n' + channel);
            // 삭제
            channelService.delete(channel.getChannelId());
            System.out.println("[채널 삭제]");
            channelService.printRemainChannel();
            System.out.println();
        }

        static void messageCRUDTest(MessageService messageService, User user, Channel channel) {
            // 생성
            Message message = messageService.create("안녕하세요.", user, channel);
            System.out.println("[메시지 생성]" + '\n' + message.toString());
            // 조회
            Message foundMessage = messageService.findById(message.getMessageId());
            System.out.println("[메시지 조회(Id)] " + '\n' + foundMessage.toString());
            List<Message> foundMessages = messageService.findAll();
            System.out.println("[메시지 전체 조회] ");
            foundMessages.forEach(System.out::println);
            // 수정
            String updatedMessage = messageService.update(message.getMessageId(), "반갑습니다.");
            System.out.println("[메시지 수정]" +  '\n' + "[변경 사항]" + '\n' +  updatedMessage);
            System.out.println();
            System.out.println("[현재 메시지] " + '\n' + message);
            // 삭재
            messageService.delete(message.getMessageId());
            System.out.println("[메세지 삭제]");
            messageService.printRemainMessages();
            System.out.println();
        }

        static User setupUser(UserService userService) {
            User setUser = userService.create("이진용","jinyoong@gmail.com", "566wrsd");
            return setUser;
        }

        static Channel setupChannel(ChannelService channelService, User user) {
            Channel channel = channelService.create(Channel.ChannelType.PUBLIC, "공지", "공지 채널입니다.", user);
            return channel;
        }

        static void messageCreateTest(MessageService messageService, User user, Channel channel) {
            Message message = messageService.create("안녕하세요.", user, channel);
            System.out.println("메시지 생성: " + message.getMessageId());
        }

        public static void main(String[] args) {
            // 서비스 초기화
//            UserService userService = new FileUserService();
//            ChannelService channelService = new FileChannelService(userService);
//            MessageService messageService = new FileMessageService(userService, channelService);

            UserRepository userRepository = new FileUserRepository();
            ChannelRepository channelRepository = new FileChannelRepository();
            MessageRepository messageRepository = new FileMessageRepository();

            UserService userService = new BasicUserService(userRepository);
            ChannelService channelService = new BasicChannelService(channelRepository);
            MessageService messageService = new BasicMessageService(messageRepository, channelRepository, userRepository);
            // 셋업
            User user = setupUser(userService);
            Channel channel = setupChannel(channelService, user);
            // 테스트
            messageCreateTest(messageService, user, channel);

            // 테스트
              userCRUDTest(userService);
              channelCRUDTest(channelService, user);
              messageCRUDTest(messageService, user, channel);

    }}


