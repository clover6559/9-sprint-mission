import entity.Channel;
import entity.Message;
import entity.User;
import service.ChannelService;
import service.serch.ChannelSearch;
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
        User user = userService.createUser("woody", "woody@codeit.com", "woody1234");
        User user2 = userService.createUser("김사연","sayeon@gmail.com", "125rtf");
        System.out.println("=== 유저 생성 ===: " + '\n' +  user2.toString());

        // 조회(ID)
        User foundUser = userService.findUserById(user.getUserId());
        System.out.println("=== 유저 조회(ID) === " +  '\n' +  foundUser.toString());
        //조건 1개(이름)
        UserSearch userSearch = new UserSearch();
        userSearch.setUserName("김사연");
        List<User> foundUser0 = userService.UserSearch(userSearch);
        System.out.println("=== 유저 조회(김사연) === " +  '\n' +  foundUser0.toString());
        //전체 유저 조회
        UserSearch userAllSearch = new UserSearch();
        List<User> foundAllUser = userService.UserSearch(userAllSearch);
        System.out.println("=== 전체 유저 조회 === " +  '\n' +  foundAllUser.toString());

        // 수정
        User updatedUser = userService.updateUser(user2.getUserId(), null, null, "woody5678");
        System.out.println("===유저 수정 === " + '\n' + updatedUser.toString());

        //삭제
        userService.deleteUser(user.getUserId());
        List<User> foundUsersAfterDelete = userService.findAllUser();
        System.out.println("=== 유저 삭제 === " +  '\n' + "남은 유저 : " +  foundUsersAfterDelete.size());
    }

    static void channelCRUDTest(ChannelService channelService) {
        User user6 = new User("김경환","gyemghwan@gmail.com", "126d25e");
        User user3 = new User("강지원","jiwon@gmail.com", "fgd123");
        // 생성
        Channel channel = channelService.createChannel(Channel.ChannelType.PUBLIC, "공지", "공지 채널입니다.", user6);
        Channel channel3 = channelService.createChannel(Channel.ChannelType.PUBLIC, "질문", "질문있어요", user3);
        Channel channel4 = channelService.createChannel(Channel.ChannelType.PUBLIC, "5조", "5조방입니다", user3);
        System.out.println("=== 채널 생성 === " +   '\n' + channel.toString());

        // 조회(ID)
        Channel foundChannel = channelService.findChannelById(channel.getChannelId());
        System.out.println("=== 채널 조회(ID) === " + '\n' + foundChannel.toString());
        //조건 1개 조회(이름)
        ChannelSearch channelSearch = new ChannelSearch();
        channelSearch.setUserName("강지원");
        List<Channel> foundChannel2 = channelService.ChannelSearch(channelSearch);
        System.out.println("=== 채널 조회(강지원) === " + '\n' + foundChannel2.toString());
        //조건 2개 조회(이름, 채널이름)
        ChannelSearch channelSearch1 = new ChannelSearch();
        channelSearch1.setUserName("강지원");
        channelSearch1.setChannelName("질문");
        List<Channel> foundChannel3 = channelService.ChannelSearch(channelSearch1);
        System.out.println("=== 채널 조회(강지원, 질문) === " + '\n' + foundChannel3.toString());

        //전체 채널 조회
        List<Channel> foundAllChannels = channelService.findAllChannel();
        System.out.println("=== 전체 채널 조회 ===  " +  '\n' + "전체 채널 : " + foundAllChannels.toString());
        //전체 채널수 조회
        List<Channel> foundChannels = channelService.findAllChannel();
        System.out.println("=== 채널 현황 조회 ===  " +  '\n' + "남은 채널 : " + foundChannels.size());

        // 수정
        Channel updatedChannel = channelService.updateChannel(channel.getChannelId(), "공지사항", null);
        System.out.println("=== 채널 수정 === " + '\n' + updatedChannel.toString());

        // 삭제
        channelService.deleteChannel(channel.getChannelId());
        List<Channel> foundChannelsAfterDelete = channelService.findAllChannel();
        System.out.println("=== 채널 삭제 ===" + '\n' + "남은 채널 : " + foundChannelsAfterDelete.size());
    }

    static void messageCRUDTest(MessageService messageService) {
        User user4 = new User("이진용","jinyoong@gmail.com", "566wrsd");
        User user5 = new User("육선우","senwoo@gmail.com", "1456d25e");
        Channel channel1 = new Channel(Channel.ChannelType.PUBLIC, "공지", "공지합니다", user4);
        Channel channel2 = new Channel(Channel.ChannelType.PRIVATE, "사담", "사담합시다", user5);
        Channel channel3 = new Channel(Channel.ChannelType.PUBLIC, "질문", "질문있어요", user4);

        // 생성
        Message message = messageService.create("안녕하세요.",user4,channel2);
        Message message1 =  messageService.create("안녕하세요",user4, channel1);
        Message message2 =  messageService.create("질문있어요",user5, channel3);
        System.out.println("=== 메시지 생성===  " + '\n' + message.toString());

        // 조회(ID)
        Message foundMessage = messageService.findMessageById(message1.getMassageId());
        System.out.println("=== 메시지 조회(단건) === " + '\n' + foundMessage.toString());
        //조건 1개 조회(이름)
        MessageSearch messageSearch = new MessageSearch();
        messageSearch.setUserName("이진용");
        List<Message> foundMessages = messageService.MessageSearch(messageSearch);
        System.out.println("=== 메시지 조회(이진용) === " + '\n' + foundMessages.toString());
        //조건 2개(이름, 채널명)
        MessageSearch messageSearch2 = new MessageSearch();
        messageSearch2.setChannelName("공지");
        messageSearch2.setUserName("이진용");
        List<Message> foundMessage2 = messageService.MessageSearch(messageSearch2);
        System.out.println("=== 메시지 조회(이름, 채널명) === " + '\n' + foundMessage2.toString());

        // 수정
        Message updatedMessage = messageService.updateMessage(message.getMassageId(), "반갑습니다.");
        System.out.println("=== 메시지 수정 === " +  '\n' + "수정 후 : " + updatedMessage.getContent() + '\n');

        // 삭제
        messageService.deleteMessage(message.getMassageId());
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


