package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.search.ChannelSearch;
import com.sprint.mission.discodeit.service.search.MessageSearch;
import com.sprint.mission.discodeit.service.search.UserSearch;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;

import static com.sprint.mission.discodeit.FileApplication.setupChannel;
import static com.sprint.mission.discodeit.FileApplication.setupUser;

@SpringBootApplication
public class DiscodeitApplication {

	static void userCRUDTest(UserService userService) {
		// 생성
		System.out.println("========= [User] =========");
		User user = userService.create("육선우","senwoo@gmail.com", "1456d25e");
		System.out.println("========= 유저 생성 =========" + '\n' +  user.toString());

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
		String updateUser = userService.update(user.getUserId(), null, null, "woody5678");
		System.out.println("=========유저 수정 ========= " + '\n' + "[변경 사항]" + '\n' + updateUser);
		System.out.println();
		System.out.println("[현재 유저 정보] " + '\n' + user);

		//삭제
		userService.delete(user.getUserId());
		System.out.println("========= 유저 삭제 =========");
		userService.printRemainUsers();
		System.out.println();
	}

	static void channelCRUDTest(ChannelService channelService, UserService userService) {
		// 생성
		System.out.println("========= [Channel] =========");
		User user = userService.create("이진용","jinyoong@gmail.com", "566wrsd");
		Channel channel = channelService.create(Channel.ChannelType.PUBLIC, "공지", "공지 채널입니다.", user);
		System.out.println("========= 채널 생성 =========" + '\n' + channel.toString());

		// 조회(ID)
		Channel foundChannel = channelService.findCById(channel.getChannelId());
		System.out.println("=========채널 조회(ID)========="+ '\n' + foundChannel.toString());
		//조건 1개 조회(이름)
		ChannelSearch channelSearch = new ChannelSearch("김사연",null);
		List<Channel> result = channelService.Search(channelSearch);
		System.out.println("========= 채널 조회(김사연) =========");
		result.forEach(System.out::println);
		//조건 2개 조회(이름, 채널이름)
		ChannelSearch channelSearch1 = new ChannelSearch("김사연", "공지");
		List<Channel> result2 = channelService.Search(channelSearch1);
		System.out.println("========= 채널 조회(김사연, 공지) =========" );
		result2.forEach(System.out::println);

		//전체 채널 조회
		List<Channel> foundAllChannels = channelService.findAllByUserId();
		System.out.println("========= 전체 채널 조회 =========");
		foundAllChannels.forEach(System.out::println);
		//전체 채널수 조회
		List<Channel> foundChannels = channelService.findCById(foundChannel.getChannelId());
		System.out.println("========= 채널 현황 조회 =========" +  '\n' + "남은 채널 : " + foundChannels.size() + "개");
		System.out.println();

		// 수정
		String updateChannel = channelService.update(channel.getChannelId(), "공지사항", null);
		System.out.println("========= 채널 수정 =========" + '\n' + "[변경 사항]" + '\n' + updateChannel);
		System.out.println();
		System.out.println("[현재 채널 정보] " + '\n' + channel);

		// 삭제
		channelService.delete(channel.getChannelId());
		System.out.println("========= 채널 삭제 =========");
		channelService.printRemainChannel();
		System.out.println();
	}

	static void messageCRUDTest(MessageService messageService, UserService userService, ChannelService channelService) {
		User user = userService.create("강지원","jinyoong@gmail.com", "566wrsd");
		User user1 = userService.create("육선우","senwoo@gmail.com", "1456d25e");
		Channel channel = channelService.create(Channel.ChannelType.PRIVATE, "사담", "사담합시다", user);

		// 생성
		Message message = messageService.create("안녕하세요.", user, channel);
		Message message1 =  messageService.create("안녕하세요", user1, channel);
		Message message2 =  messageService.create("뭐하시나요", user, channel);
		System.out.println("========= [Message] =========");
		System.out.println("========= 메시지 생성 =========" + '\n' + message.toString());

		// 조회(ID)
		Message foundMessage = messageService.findById(message1.getMessageId());
		System.out.println("========= 메시지 조회(단건) =========" + '\n' + foundMessage.toString());
		//조건 1개 조회(이름)
		MessageSearch Search1 = new MessageSearch("강지원", null);
		List<Message> foundMessages = messageService.Search(Search1);
		System.out.println("========= 메시지 조회(강지원) =========");
		foundMessages.forEach(System.out::println);

		//조건 2개(이름, 채널명)
		MessageSearch messageSearch2 = new MessageSearch("강지원","사담");
		List<Message> foundMessage2 = messageService.Search(messageSearch2);
		System.out.println("========= 메시지 조회(강지원, 사담) =========");
		foundMessage2.forEach(System.out::println);
		//전체 메세지 조회
		List<Message> foundAllMessages = messageService.findAllByChannelId();
		System.out.println("========= 전체 메세지 조회 =========");
		foundAllMessages.forEach(System.out::println);

		// 수정
		String updateMessage = messageService.update(message.getMessageId(), "반갑습니다.");
		System.out.println("========= 메시지 수정 =========" +  '\n' +"[변경 사항]" + '\n' + updateMessage);
		System.out.println();
		System.out.println("[현재 메시지] " + '\n' + message);

		// 삭제
		messageService.delete(message.getMessageId());
		System.out.println("========= 메세지 삭제 =========");
		messageService.printRemainMessages();
		System.out.println();
	}


	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

		UserService userService = context.getBean(UserService.class);
		ChannelService channelService = context.getBean(ChannelService.class);
		MessageService messageService = context.getBean(MessageService.class);

		User user = setupUser(userService);
		Channel channel = setupChannel(channelService, user);
		// 테스트
		userCRUDTest(userService);
		channelCRUDTest(channelService, userService);
		messageCRUDTest(messageService, userService, channelService);


	}

}
