import entity.Channel;
import entity.Message;
import entity.User;
import service.ChannelService;
import service.MessageService;
import service.UserService;
import service.jcf.JCFChannelService;
import service.jcf.JCFMessageService;
import service.jcf.JCFUserService;

import java.util.List;
import java.util.UUID;

public class JavaApplication {
    static void userCRUDTest(UserService userService) {
        // 생성
        User user = userService.create("woody", "woody@codeit.com", "woody1234");
        System.out.println("유저 생성: " + user.getUserId());
        // 조회
        User foundUser = userService.find(user.getUserId());
        System.out.println("유저 조회(단건): " + foundUser.getUserId());
        List<User> foundUsers = userService.findAll();
        System.out.println("유저 조회(다건): " + foundUsers.size());
        // 수정
        User updatedUser = userService.update(user.getUserId(), null, null, "woody5678");
        System.out.println("유저 수정: " + String.join("/", updatedUser.getUserName(), updatedUser.getEmail(), updatedUser.getPassword()));
        // 삭제
        userService.delete(user.getUserId());
        List<User> foundUsersAfterDelete = userService.findAll();
        System.out.println("유저 삭제: " + foundUsersAfterDelete.size());
    }

    static void channelCRUDTest(ChannelService channelService) {
        // 생성
        Channel channel = channelService.create(Channel.ChannelType.PUBLIC, "공지", "공지 채널입니다.");
        System.out.println("채널 생성: " + channel.getChannelId());
        // 조회
        Channel foundChannel = channelService.find(channel.getChannelId());
        System.out.println("채널 조회(단건): " + foundChannel.getChannelId());
        List<Channel> foundChannels = channelService.findAll();
        System.out.println("채널 조회(다건): " + foundChannels.size());
        // 수정
        Channel updatedChannel = channelService.update(channel.getChannelId(), "공지사항", null);
        System.out.println("채널 수정: " + String.join("/", channel.getUserName(), updatedChannel.getDescription()));
        // 삭제
        channelService.delete(channel.getChannelId());
        List<Channel> foundChannelsAfterDelete = channelService.findAll();
        System.out.println("채널 삭제: " + foundChannelsAfterDelete.size());
    }

    static void messageCRUDTest(MessageService messageService) {
        // 생성
        Message message = messageService.create("안녕하세요.", message.getChannelId(), message.getUserId());
        System.out.println("메시지 생성: " + message.getUserId());
        // 조회
        Message foundMessage = messageService.find(message.getUserId());
        System.out.println("메시지 조회(단건): " + foundMessage.getUserId());
        List<Message> foundMessages = messageService.findAll();
        System.out.println("메시지 조회(다건): " + foundMessages.size());
        // 수정
        Message updatedMessage = messageService.update(message.getUserId(), "반갑습니다.");
        System.out.println("메시지 수정: " + updatedMessage.getContent());
        // 삭재
        messageService.delete(message.getUserId());
        List<Message> foundMessagesAfterDelete = messageService.findAll();
        System.out.println("메시지 삭제: " + foundMessagesAfterDelete.size());
    }

    public static void main(String[] args) {
        // 서비스 초기화
        UserService userService = new JCFUserService();
        ChannelService channelService = new JCFChannelService();
        MessageService messageService = new JCFMessageService();

        // 테스트
        userCRUDTest(userService);
        channelCRUDTest(channelService);
        messageCRUDTest(messageService);
    }
}


