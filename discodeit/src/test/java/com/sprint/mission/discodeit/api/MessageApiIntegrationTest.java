package com.sprint.mission.discodeit.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Channel.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class MessageApiIntegrationTest {

    @Autowired protected MockMvc mockMvc;

    @Autowired protected ObjectMapper objectMapper;
    @Autowired private MessageRepository messageRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ChannelRepository channelRepository;

    private User author;
    private Channel channel;

    @BeforeEach
    void setUp() {
        // 모든 테스트에서 공통으로 사용할 유저와 채널을 미리 만들어둬.
        String id = UUID.randomUUID().toString().substring(0, 8);
        User user =
                User.builder()
                        .username("sender_" + id)
                        .email("s_" + id + "@test.com")
                        .password("password123")
                        .build();
        UserStatus status = new UserStatus(user, Instant.now());
        author = userRepository.save(user);
        channel = channelRepository.save(new Channel(ChannelType.PUBLIC, "테스트채널", "설명"));
    }

    @Test
    @DisplayName("메시지를 생성한다. (첨부파일 포함)")
    void createMessage() throws Exception {
        // 1. Given: 요청 DTO 준비
        MessageCreateRequest request =
                new MessageCreateRequest("안녕하세요!", channel.getId(), author.getId());

        MockMultipartFile requestPart =
                new MockMultipartFile(
                        "messageCreateRequest", // 컨트롤러의 @RequestPart 이름!
                        "",
                        MediaType.APPLICATION_JSON_VALUE,
                        objectMapper.writeValueAsString(request).getBytes(StandardCharsets.UTF_8));

        MockMultipartFile filePart =
                new MockMultipartFile(
                        "attachments", // 컨트롤러의 MultipartFile 리스트 이름!
                        "test.txt",
                        MediaType.TEXT_PLAIN_VALUE,
                        "hello world".getBytes());

        // 2. When & Then
        mockMvc.perform(
                        multipart("/api/messages")
                                .file(requestPart)
                                .file(filePart)
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value("안녕하세요!"));
    }

    @Test
    @DisplayName("메시지 내용을 수정한다.")
    void updateMessage() throws Exception {
        // 1. Given: 수정할 메시지 미리 저장
        Message message = messageRepository.save(new Message("기존 내용", channel, author, null));
        MessageUpdateRequest request = new MessageUpdateRequest("수정된 내용");

        // 2. When & Then: 수정은 @RequestBody이므로 put/patch 사용
        mockMvc.perform(
                        patch("/api/messages/{messageId}", message.getId())
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("수정된 내용"));
    }

    @Test
    @DisplayName("채널 내 메시지 목록을 커서 기반 페이징으로 조회한다.")
    void findByChannelId() throws Exception {
        // 1. Given: 메시지 2개 생성
        messageRepository.save(new Message("첫 번째 메시지", channel, author, null));
        messageRepository.save(new Message("두 번째 메시지", channel, author, null));

        // 2. When & Then
        mockMvc.perform(
                        get("/api/messages")
                                .param("channelId", channel.getId().toString())
                                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2));
    }

    @Test
    @DisplayName("메시지를 삭제한다.")
    void deleteMessage() throws Exception {
        // 1. Given
        Message message = messageRepository.save(new Message("삭제될 메시지", channel, author, null));

        // 2. When & Then
        mockMvc.perform(delete("/api/messages/{messageId}", message.getId()))
                .andExpect(status().isNoContent());
    }
}
