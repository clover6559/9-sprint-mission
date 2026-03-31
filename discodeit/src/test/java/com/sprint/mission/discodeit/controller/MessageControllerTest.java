package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.api.controller.MessageController;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.exception.Message.MessageNotFoundException;
import com.sprint.mission.discodeit.service.MessageService;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MessageController.class)
class MessageControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private MessageService messageService;

  @Test
  @DisplayName("메시지 생성 성공")
  void createMessage_success() throws Exception {
    MessageCreateRequest request = new MessageCreateRequest("하이", UUID.randomUUID(),
        UUID.randomUUID());
    String requestJson = objectMapper.writeValueAsString(request);

    MockMultipartFile mockMultipartFile = new MockMultipartFile(
        "messageCreateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        requestJson.getBytes(StandardCharsets.UTF_8));

    MessageDto responseDto =
        new MessageDto(UUID.randomUUID(), Instant.now(), Instant.now(), "하이", UUID.randomUUID(),
            null, null);
    given(messageService.create(any(), any())).willReturn(responseDto);

    mockMvc.perform(multipart("/api/messages").file(mockMultipartFile)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.content").value("하이"));
  }

  @Test
  @DisplayName("채널별 메세지 목록 조회 성공")
  void findByChannelId_success() throws Exception {
    UUID channelId = UUID.randomUUID();
    UserDto user = new UserDto(UUID.randomUUID(), "name@name.com", "name", null, false);
    MessageDto messageDto =
        new MessageDto(UUID.randomUUID(), Instant.now(), Instant.now(), "안녕하세요", channelId, user,
            null);
    PageResponse<MessageDto> pageResponse = new PageResponse<>(List.of(messageDto), false, 10, true,
        20L);
    given(messageService.findAllByChannelId(eq(channelId), any(), any())).willReturn(pageResponse);

    mockMvc.perform(get("/api/messages")
            .param("channelId", channelId.toString())
            .param("size", "10")
            .param("sort", "createdAt, DESC")
            .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.size()").value(1))
        .andExpect(jsonPath("$.content[0].content").value("안녕하세요"));
  }

  @Test
  @DisplayName("메시지 업데이트 실패 - 메시지 없음 (404 Not Found)")
  void update_Fail_NotFound() throws Exception {
    UUID messageId = UUID.randomUUID();
    MessageUpdateRequest request = new MessageUpdateRequest("Updated Content");

    given(messageService.update(eq(messageId), any())).willThrow(
        new MessageNotFoundException(messageId));

    mockMvc.perform(patch("/api/messages/{messageId}", messageId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isNotFound());
  }
}
