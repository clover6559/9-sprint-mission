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
        User user = userService.create("육선우","senwoo@gmail.com", "1456d25e");
        System.out.println("========= [User] =========");
        System.out.println("========= 유저 생성 ========= " + '\n' +  user.toString());

        // 조회(ID)
        User foundUser = userService.findById(user.getUserId());
        System.out.println("========= 유저 조회(ID) =========" +  '\n' +  foundUser.toString());
        //조건 1개(이름)
        UserSearch userSearch = new UserSearch("김사연");
        List<User> foundUserByName = userService.Search(userSearch);
        System.out.println("=========유저 조회(김사연) =========" );
        foundUserByName.forEach(System.out::println);
        //전체 유저 조회
        List<User> foundAllUsers = userService.findAll();
        System.out.println("========= 전체 유저 조회 =========");
        foundAllUsers.forEach(System.out::println);

        // 수정
        String updatedUser = userService.update(user.getUserId(), null, null, "woody5678");
        System.out.println("=========유저 수정 ========= " + '\n' + "[변경 사항]" + '\n' +  updatedUser);

        //삭제
        userService.delete(user.getUserId());
        List<User> foundUsersAfterDelete = userService.findAll();
        System.out.println("========= 유저 삭제 ========= " +  '\n' + "남은 유저 : " +  foundUsersAfterDelete.size() + "명");
        System.out.println();
    }

    static void channelCRUDTest(ChannelService channelService, UserService userService) {
        // 생성
        User testUser = userService.findAll().get(0);
        Channel channel = channelService.create(Channel.ChannelType.PUBLIC, "공지", "공지 채널입니다.", testUser);
        System.out.println("========= [Channel] =========");
        System.out.println("========= 채널 생성 =========" +   '\n' + channel.toString());

        // 조회(ID)
        Channel foundChannel = channelService.findCById(channel.getChannelId());
        System.out.println("=========채널 조회(ID)========="+ '\n' + foundChannel.toString());
        //조건 1개 조회(이름)
        ChannelSearch channelSearch = new  ChannelSearch("강지원",null);
        List<Channel> result = channelService.Search(channelSearch);
        System.out.println("========= 채널 조회(강지원) =========");
        result.forEach(System.out::println);
        //조건 2개 조회(이름, 채널이름)
        ChannelSearch channelSearch1 = new ChannelSearch("강지원", "질문");
        List<Channel> result2 = channelService.Search(channelSearch);
        System.out.println("========= 채널 조회(강지원, 질문) =========" );
        result2.forEach(System.out::println);

        //전체 채널 조회
        List<Channel> foundAllChannels = channelService.findAll();
        System.out.println("========= 전체 채널 조회 =========");
        foundAllChannels.forEach(System.out::println);
        //전체 채널수 조회
        List<Channel> foundChannels = channelService.findAll();
        System.out.println("========= 채널 현황 조회 =========" +  '\n' + "남은 채널 : " + foundChannels.size() + "개");
        System.out.println();

        // 수정
        Channel updatedChannel = channelService.update(channel.getChannelId(), "공지사항", null);
        System.out.println("========= 채널 수정 =========" + '\n' + updatedChannel.toString());

        // 삭제
        channelService.delete(channel.getChannelId());
        List<Channel> foundChannelsAfterDelete = channelService.findAll();
        System.out.println("========= 채널 삭제= ========" + '\n' + "남은 채널 : " + foundChannelsAfterDelete.size() + "개");
        System.out.println();
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
        System.out.println("========= [Message] =========");
        System.out.println("========= 메시지 생성 =========" + '\n' + message.toString());

        // 조회(ID)
        Message foundMessage = messageService.findById(message1.getMessageId());
        System.out.println("========= 메시지 조회(단건) =========" + '\n' + foundMessage.toString());
        //조건 1개 조회(이름)
        MessageSearch Search1 = new MessageSearch("강지원", null);
        List<Message> foundMessages = messageService.Search(Search1);
        System.out.println("========= 메시지 조회(이진용) =========");
        foundMessages.forEach(System.out::println);

        //조건 2개(이름, 채널명)
        MessageSearch messageSearch2 = new MessageSearch("이진용","공지");
        List<Message> foundMessage2 = messageService.Search(messageSearch2);
        System.out.println("========= 메시지 조회(이름, 채널명) =========");
        foundMessage2.forEach(System.out::println);

        // 수정
        Message updatedMessage = messageService.update(message.getMessageId(), "반갑습니다.");
        System.out.println("========= 메시지 수정 =========" +  '\n' + "수정 후 : " + updatedMessage.getContent());
        System.out.println();

        // 삭제
        messageService.delete(message.getMessageId());
        List<Message> foundMessagesAfterDelete = messageService.Search(Search1);
        System.out.println("========= 메세지 삭제 =========");
        System.out.println("남은 메세지 수 : " + foundMessagesAfterDelete.size() + "개");
//        System.out.println("남은 메세지 내용 : " + foundMessagesAfterDelete.containsAll(message.) + "개");

    }

    public static void main(String[] args) {
        // 서비스 초기화
        UserService userService = new JCFUserService();
        ChannelService channelService = new JCFChannelService(userService);
        MessageService messageService = new JCFMessageService(userService, channelService);

        // 테스트
        userCRUDTest(userService);
        channelCRUDTest(channelService, userService);
        messageCRUDTest(messageService);
    }
}


