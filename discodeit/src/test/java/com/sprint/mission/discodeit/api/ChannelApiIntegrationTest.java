package com.sprint.mission.discodeit.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.request.CreatePrivateChannelRequest;
import com.sprint.mission.discodeit.dto.request.CreatePublicChannelRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Channel.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class ChannelApiIntegrationTest {

  @Autowired
  protected MockMvc mockMvc;

  @Autowired
  protected ObjectMapper objectMapper;

  @Autowired
  private ChannelRepository channelRepository;

  @Autowired
  private UserRepository userRepository;

  private User createUniqueUser() {
    String id = UUID.randomUUID().toString().substring(0, 8);
    return userRepository.save(User.builder()
        .username("user_" + id)
        .email(id + "@test.com")
        .password("password123")
        .build());
  }

  @Test
  @DisplayName("공개 채널을 생성한다.")
  void createPublicChannel() throws Exception {
    // given
    CreatePublicChannelRequest request = new CreatePublicChannelRequest("공개채널", "채널 설명입니다.");

    // when & then
    mockMvc.perform(post("/api/channels/public")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("공개채널"));
  }

  @Test
  @DisplayName("비공개 채널을 생성한다.")
  void createPrivateChannel() throws Exception {
    // given: 참여할 유저 2명 준비
    String id1 = UUID.randomUUID().toString().substring(0, 8);
    User user1 = userRepository.save(User.builder()
        .username("member1_" + id1)
        .email("m1_" + id1 + "@test.com")
        .password("password123")
        .build());

    String id2 = UUID.randomUUID().toString().substring(0, 8);
    User user2 = userRepository.save(User.builder()
        .username("member2_" + id2)
        .email("m2_" + id2 + "@test.com")
        .password("password123")
        .build());
    List<UUID> participantIds = List.of(user1.getId(), user2.getId());

    CreatePrivateChannelRequest request = new CreatePrivateChannelRequest(participantIds);

    // when & then
    mockMvc.perform(post("/api/channels/private")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());
  }

  @Test
  @DisplayName("채널 정보를 업데이트한다.")
  void updateChannel() throws Exception {
    // given: 먼저 채널 하나 생성 (Public 채널 예시)
    Channel channel = channelRepository.save(new Channel(ChannelType.PUBLIC, "기존채널", "설명"));
    UUID channelId = channel.getId();

    ChannelUpdateRequest request = new ChannelUpdateRequest("수정된채널", "수정된 설명");

    // when & then
    mockMvc.perform(patch("/api/channels/{channelId}", channelId)
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("수정된채널"));
  }

  @Test
  @DisplayName("유저 ID로 참여 중인 채널 목록을 조회한다.")
  void findAllByUserId() throws Exception {
    // given: 유저 생성 및 해당 유저가 포함된 채널 데이터 준비 로직 필요
    // (조회 API는 Service 구현 방식에 따라 Given 데이터를 더 정밀하게 세팅해야 할 수 있어!)
    User user = createUniqueUser();

    // when & then
    mockMvc.perform(get("/api/channels")
            .param("userId", user.getId().toString())) // 쿼리 파라미터 전달
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("채널을 삭제한다.")
  void deleteChannel() throws Exception {
    // given
    Channel channel = channelRepository.save(new Channel(ChannelType.PUBLIC, "삭제할채널", "설명"));
    UUID channelId = channel.getId();

    // when & then
    mockMvc.perform(delete("/api/channels/{channelId}", channelId))
        .andExpect(status().isNoContent());
  }
}
