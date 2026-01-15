import entity.Channel;
import entity.Message;
import entity.User;
import service.ChannelService;
import service.serch.MessageSearch;
import service.MessageService;
import service.UserService;
import service.jcf.JCFChannelService;
import service.jcf.JCFMessageService;
import service.jcf.JCFUserService;
import service.serch.UserSearch;

import java.util.List;

public class JavaApplication {

    static void userCRUDTest(UserService userService) {
        // 생성
        User user = userService.create("woody", "woody@codeit.com", "woody1234");
        User user2 = userService.create("김사연","sayeon@gmail.com", "125rtf");
        System.out.println("=== 유저 생성 ===: " + '\n' +  user.toString());

        // 조회(단건)
        User foundUser = userService.find(user.getUserId());
        System.out.println("=== 유저 조회(단건) === " +  '\n' +  foundUser.toString());

        UserSearch userSearch = new UserSearch();
        userSearch.setUserName("김사연");
        List<User> foundUser0 = userService.UserSerch(userSearch);
        System.out.println("=== 유저 조회(단건) === " +  '\n' +  foundUser0.toString());

        UserSearch userAllSerch = new UserSearch();
        List<User> foundAllUser = userService.UserSerch(userAllSerch);
        System.out.println("=== 유저 조회(전체) === " +  '\n' +  foundAllUser.toString());

//        List<User> foundUsers = userService.findAll();
//        System.out.println("=== 유저 조회(전체) === " + '\n' + foundUsers.toString());
        // 수정
        User updatedUser = userService.update(user2.getUserId(), null, null, "woody5678");
        System.out.println("===유저 수정 === " + '\n' + updatedUser.toString());

        userService.delete(user.getUserId());
        List<User> foundUsersAfterDelete = userService.findAll();
        System.out.println("=== 유저 삭제 === " +  '\n' + "남은 유저 : " +  foundUsersAfterDelete.size());
    }

    static void channelCRUDTest(ChannelService channelService) {
        User user6 = new User("김경환","gyemghwan@gmail.com", "126d25e");
        User user3 = new User("강지원","jiwon@gmail.com", "fgd123");
        // 생성
        Channel channel = channelService.create(Channel.ChannelType.PUBLIC, "공지", "공지 채널입니다.", user6);
        System.out.println("=== 채널 생성 === " +   '\n' + channel.toString());
        // 조회
        Channel foundChannel = channelService.find(channel.getChannelId());
        System.out.println("=== 채널 조회(단건) === " + '\n' + foundChannel.toString());

        List<Channel> foundChannels = channelService.findAll();
        System.out.println("=== 채널 현황 조회 ===  " +  '\n' + "남은 채널 : " + foundChannels.size());
        // 수정
        Channel updatedChannel = channelService.update(channel.getChannelId(), "공지사항", null);
        System.out.println("=== 채널 수정 === " + '\n' + updatedChannel.toString());
        // 삭제
        channelService.delete(channel.getChannelId());
        List<Channel> foundChannelsAfterDelete = channelService.findAll();
        System.out.println("=== 채널 삭제 ===" + '\n' + "남은 채널 : " + foundChannelsAfterDelete.size());
    }

    static void messageCRUDTest(MessageService messageService) {
        User user4 = new User("이진용","jinyoong@gmail.com", "566wrsd");
        User user5 = new User("육선우","senwoo@gmail.com", "1456d25e");
        Channel channel1 = new Channel(Channel.ChannelType.PUBLIC, "공지", "공지합니다", user4);
        Channel channel2 = new Channel(Channel.ChannelType.PRIVATE, "사담", "사담합시다", user5);

        // 생성
        Message message = messageService.create("안녕하세요.",user4,channel2);
        Message message1 =  messageService.create("안녕하세요",user4, channel1);
        System.out.println("=== 메시지 생성===  " + '\n' + message.toString());

        // 조회
        Message foundMessage = messageService.find(message1.getMassageId());
        System.out.println("=== 메시지 조회(단건) === " + '\n' + foundMessage.toString());

        MessageSearch messageSearch = new MessageSearch();
        messageSearch.setUserName("진");
        List<Message> foundMessages = messageService.MessageSearch(messageSearch);
        System.out.println("=== 메시지 조회(다건) === " + '\n' + foundMessages.toString() + "남은 메세지 : " + foundMessages.size());

        messageSearch = new MessageSearch();
        messageSearch.setUserName("진");
        messageSearch.setChannelName("공지");
        List<Message> foundMessagefilter = messageService.MessageSearch(messageSearch);
        System.out.println("=== 메시지 조회(다건) === " + '\n' + foundMessagefilter.toString() + "남은 메세지 : " + foundMessagefilter.size());

        // 수정
        Message updatedMessage = messageService.update(message.getMassageId(), "반갑습니다.");
        System.out.println("=== 메시지 수정 === " +  '\n' + updatedMessage.getContent() + '\n');
        // 삭제
        messageService.delete(message.getMassageId());
        List<Message> foundMessagesAfterDelete = messageService.MessageSearch(messageSearch);
        System.out.println("남은 메세지 : " + foundMessagesAfterDelete);
    }

    public static void main(String[] args) {
        // 서비스 초기화
        UserService userService = new JCFUserService();
        ChannelService channelService = new JCFChannelService(userService);
        MessageService messageService = new JCFMessageService(userService, channelService);

        // 테스트
        userCRUDTest(userService);
        channelCRUDTest(channelService);
        messageCRUDTest(messageService);
    }
}


