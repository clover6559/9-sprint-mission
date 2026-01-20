import entity.Channel;
import entity.Message;
import entity.User;
import service.ChannelService;
import service.MessageService;
import service.UserService;
import service.file.FileChannelService;
import service.file.FileMessageService;
import service.file.FileUserService;

import java.util.List;

public class FileApplication {
        static void userCRUDTest(UserService userService) {
            // 생성
            User user = userService.createUser("woody", "woody@codeit.com", "woody1234");
            System.out.println("유저 생성: " + user.getUserId());
            // 조회
            User foundUser = userService.findUserById(user.getUserId());
            System.out.println("유저 조회(단건): " + foundUser.getUserId());
            List<User> foundUsers = userService.findAllUser();
            System.out.println("유저 조회(다건): " + foundUsers.size());
            // 수정
            User updatedUser = userService.updateUser(user.getUserId(), null, null, "woody5678");
            System.out.println("유저 수정: " + String.join("/", updatedUser.getUserName(), updatedUser.getEmail(), updatedUser.getPassword()));
            // 삭제
//            userService.deleteById(user.getUserId());
//            List<User> foundUsersAfterDelete = userService.findAllUser();
//            System.out.println("유저 삭제: " + foundUsersAfterDelete.size());
        }

        static void channelCRUDTest(ChannelService channelService, User user) {
            // 생성
            Channel channel = channelService.createChannel(Channel.ChannelType.PUBLIC, "공지", "공지 채널입니다.", user);
            System.out.println("채널 생성: " + channel.getChannelId());
            // 조회
            Channel foundChannel = channelService.findChannelById(channel.getChannelId());
            System.out.println("채널 조회(단건): " + foundChannel.getChannelId());
            List<Channel> foundChannels = channelService.findAllChannel();
            System.out.println("채널 조회(다건): " + foundChannels.size());
            // 수정
            Channel updatedChannel = channelService.updateChannel(channel.getChannelId(), "공지사항", null);
            System.out.println("채널 수정: " + String.join("/", updatedChannel.getChannelName(), updatedChannel.getDescription()));
            // 삭제
//            channelService.deleteChannel(channel.getChannelId());
//            List<Channel> foundChannelsAfterDelete = channelService.findAllChannel();
//            System.out.println("채널 삭제: " + foundChannelsAfterDelete.size());
        }

        static void messageCRUDTest(MessageService messageService, User user, Channel channel) {
            // 생성
            Message message = messageService.create("안녕하세요.", user, channel);
            System.out.println("메시지 생성: " + message.getMassageId());
            // 조회
            Message foundMessage = messageService.findMessageById(message.getMassageId());
            System.out.println("메시지 조회(단건): " + foundMessage.getMassageId());
            List<Message> foundMessages = messageService.findAllMessage();
            System.out.println("메시지 조회(다건): " + foundMessages.size());
            // 수정
            Message updatedMessage = messageService.updateMessage(message.getMassageId(), "반갑습니다.");
            System.out.println("메시지 수정: " + updatedMessage.getContent());
            // 삭재
//            messageService.deleteMessage(message.getMassageId());
//            List<Message> foundMessagesAfterDelete = messageService.findAllMessage();
//            System.out.println("메시지 삭제: " + foundMessagesAfterDelete.size());
        }

        static User setupUser(UserService userService) {
            User setUser = userService.createUser("woody", "woody@codeit.com", "woody1234");
            return setUser;
        }

        static Channel setupChannel(ChannelService channelService, User user) {
            Channel channel = channelService.createChannel(Channel.ChannelType.PUBLIC, "공지", "공지 채널입니다.", user);
            return channel;
        }

        static void messageCreateTest(MessageService messageService, User user, Channel channel) {
            Message message = messageService.create("안녕하세요.", user, channel);
            System.out.println("메시지 생성: " + message.getMassageId());
        }

        public static void main(String[] args) {
            // 서비스 초기화
            UserService userService = new FileUserService();
            ChannelService channelService = new FileChannelService(userService);
            MessageService messageService = new FileMessageService(userService, channelService);

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


