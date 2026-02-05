package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdate;
import com.sprint.mission.discodeit.dto.channel.CreatePrivate;
import com.sprint.mission.discodeit.dto.channel.CreatePublic;
import com.sprint.mission.discodeit.dto.message.MessageCreate;
import com.sprint.mission.discodeit.dto.message.MessageUpdate;
import com.sprint.mission.discodeit.dto.user.UserCreate;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.dto.user.UserUpdate;
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

@SpringBootApplication
public class DiscodeitApplication {

	static void userCRUDTest(UserService userService) {
		// 생성
		System.out.println("========= [User] =========");
		UserCreate.BasicUserInfo userInfo = new UserCreate.BasicUserInfo("육선우","senwoo@gmail.com", "1456d25e");
		UserCreate create = new UserCreate(userInfo, null);
		User user = userService.create(create);
		System.out.println("========= 유저 생성 =========" + '\n' +  user.toString());

		// 조회(ID)
		UserResponse foundUser = userService.findById(user.getId());
		System.out.println("========= 유저 조회(ID) =========" +  '\n' +  foundUser.toString());
		//조건 1개(이름)
		UserSearch userSearch = new UserSearch("육선우");
		List<User> foundUserByName = userService.search(userSearch);
		System.out.println("=========유저 조회(육선우) =========" );
		foundUserByName.forEach(System.out::println);
		//전체 유저 조회
		List<UserResponse> foundAllUsers = userService.findAll();
		System.out.println("========= 전체 유저 조회 =========");
		foundAllUsers.forEach(System.out::println);

		// 수정
		UserUpdate.UserUpdateInfo updateInfo = new UserUpdate.UserUpdateInfo(null, null, "woody5678", null);
		UserUpdate update = new UserUpdate(user.getId(), updateInfo);
		String updateMessage = userService.update(update);
		System.out.println("=========유저 수정 ========= " + '\n' + "[변경 사항]" + '\n' + updateMessage);
		System.out.println();
		System.out.println("[현재 유저 정보] " + '\n' + update);

		//삭제
		userService.delete(user.getId());
		System.out.println("========= 유저 삭제 =========");
		userService.printRemainUsers();
		System.out.println();
	}

	static void channelCRUDTest(ChannelService channelService, UserService userService) {
		// 생성
		System.out.println("========= [Channel] =========");
		UserCreate.BasicUserInfo userInfo = new UserCreate.BasicUserInfo("이진용","jinyoong@gmail.com", "566wrsd");
		UserCreate create = new UserCreate(userInfo, null);
		User user = userService.create(create);
		CreatePublic createPublic = new CreatePublic("공지", "공지 채널입니다.", user);
		Channel channel = channelService.createPublicChannel(createPublic);
		System.out.println("========= 채널 생성 =========" + '\n' + channel.toString());

		// 조회(ID)
		ChannelResponse foundChannel = channelService.findById(channel.getId());
		System.out.println("=========채널 조회(ID)========="+ '\n' + foundChannel.toString());
		//조건 1개 조회(이름)
		ChannelSearch channelSearch = new ChannelSearch("이진용",null, Channel.ChannelType.PUBLIC);
		List<Channel> result = channelService.search(channelSearch);
		System.out.println("========= 채널 조회(이진용) =========");
		result.forEach(System.out::println);
		//조건 2개 조회(이름, 채널이름)
		ChannelSearch channelSearch1 = new ChannelSearch("이진용", "공지", Channel.ChannelType.PRIVATE);
		List<Channel> result2 = channelService.search(channelSearch1);
		System.out.println("========= 채널 조회(이진용, Public) =========" );
		result2.forEach(System.out::println);

		//전체 채널 조회
		List<ChannelResponse> foundAllChannels = channelService.findAllByUserId(user.getId());
		System.out.println("========= 전체 채널 조회 =========");
		foundAllChannels.forEach(System.out::println);

		// 수정
		ChannelUpdate.ChannelUpdateInfo channelUpdateInfo= new ChannelUpdate.ChannelUpdateInfo("공지사항", null);
		ChannelUpdate update = new ChannelUpdate(channel.getId(), channelUpdateInfo);
		String updateMessage = channelService.update(update);
		System.out.println("========= 채널 수정 =========" + '\n' + "[변경 사항]" + '\n' + updateMessage);
		System.out.println();
		System.out.println("[현재 채널 정보] " + '\n' + channel);

		// 삭제
		channelService.delete(channel.getId());
		System.out.println("========= 채널 삭제 =========");
		channelService.printRemainChannel();
		System.out.println();
	}

	static void messageCRUDTest(MessageService messageService, UserService userService, ChannelService channelService) {
		User user = userService.create(new UserCreate(new UserCreate.BasicUserInfo("강지원","jiwon@gmail.com", "566wrsd"), null));
		User user1 = userService.create(new UserCreate(new UserCreate.BasicUserInfo("육선우","senwoo@gmail.com", "1456d25e"), null));
		Channel channel = channelService.createPublicChannel(new CreatePublic("공지","공지", user));

		// 생성
		MessageCreate.BasicMessageInfo messageInfo = new MessageCreate.BasicMessageInfo(channel.getId(),user.getId(),"안녕하세요.",null);
		MessageCreate createDto = new MessageCreate(messageInfo, null);
		MessageCreate.BasicMessageInfo messageInfo1 = new MessageCreate.BasicMessageInfo(channel.getId(),user1.getId(),"뭐하시나요.",null);
		MessageCreate createDto1 = new MessageCreate(messageInfo1, null);
		System.out.println("========= [Message] =========");
		Message message = messageService.create(createDto);
		System.out.println("========= 메시지 생성 =========" + '\n' + message.toString());

		// 조회(ID)
		Message foundMessage = messageService.findById(message.getId());
		System.out.println("========= 메시지 조회(단건) =========" + '\n' + foundMessage.toString());
		//조건 1개 조회(이름)
		System.out.println("========= 메시지 조회(강지원) =========");
		messageService.search(new MessageSearch("강지원", null))
				.forEach(System.out::println);

		//조건 2개(이름, 채널명)
		System.out.println("========= 메시지 조회(강지원, 공지) =========");
		messageService.search(new MessageSearch("강지원","공지"))
				.forEach(System.out::println);
		//전체 메세지 조회
		List<Message> foundAllMessages = messageService.findAllByChannelId(channel.getId());
		System.out.println("========= 전체 메세지 조회 =========");
		foundAllMessages.forEach(System.out::println);

		// 수정
		MessageUpdate update = new MessageUpdate(message.getId(), "반갑습니다.");
		String updateMessage = messageService.update(update);
		System.out.println("========= 메시지 수정 =========" +  '\n' +"[변경 사항]" + '\n' + updateMessage);
		System.out.println();
		System.out.println("[현재 메시지] " + '\n' + message);

		// 삭제
		messageService.delete(message.getId());
		System.out.println("========= 메세지 삭제 =========");
		messageService.printRemainMessages();
		System.out.println();
	}


	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

		UserService userService = context.getBean(UserService.class);
		ChannelService channelService = context.getBean(ChannelService.class);
		MessageService messageService = context.getBean(MessageService.class);

//		User user = setupUser(userService);
//		Channel channel = setupChannel(channelService, user);
		// 테스트
		userCRUDTest(userService);
		channelCRUDTest(channelService, userService);
		messageCRUDTest(messageService, userService, channelService);


	}

}
