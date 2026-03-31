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

  @Autowired
  protected MockMvc mockMvc;

  @Autowired
  protected ObjectMapper objectMapper;

  @Autowired
  private MessageRepository messageRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ChannelRepository channelRepository;

  private User author;
  private Channel channel;

  @BeforeEach
  void setUp() {
    String id = UUID.randomUUID().toString().substring(0, 8);
    User user = User.builder()
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
    MessageCreateRequest request = new MessageCreateRequest("안녕하세요!", channel.getId(),
        author.getId());

    MockMultipartFile requestPart = new MockMultipartFile(
        "messageCreateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsString(request).getBytes(StandardCharsets.UTF_8));

    MockMultipartFile filePart = new MockMultipartFile(
        "attachments",
        "test.txt",
        MediaType.TEXT_PLAIN_VALUE,
        "hello world".getBytes());

    mockMvc.perform(multipart("/api/messages")
            .file(requestPart)
            .file(filePart)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.content").value("안녕하세요!"));
  }

  @Test
  @DisplayName("메시지 내용을 수정한다.")
  void updateMessage() throws Exception {
    Message message = messageRepository.save(new Message("기존 내용", channel, author, null));
    MessageUpdateRequest request = new MessageUpdateRequest("수정된 내용");

    mockMvc.perform(patch("/api/messages/{messageId}", message.getId())
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").value("수정된 내용"));
  }

  @Test
  @DisplayName("채널 내 메시지 목록을 커서 기반 페이징으로 조회한다.")
  void findByChannelId() throws Exception {
    messageRepository.save(new Message("첫 번째 메시지", channel, author, null));
    messageRepository.save(new Message("두 번째 메시지", channel, author, null));

    mockMvc.perform(get("/api/messages")
            .param("channelId", channel.getId().toString())
            .param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()").value(2));
  }

  @Test
  @DisplayName("메시지를 삭제한다.")
  void deleteMessage() throws Exception {
    Message message = messageRepository.save(new Message("삭제될 메시지", channel, author, null));

    mockMvc.perform(delete("/api/messages/{messageId}", message.getId()))
        .andExpect(status().isNoContent());
  }
}
