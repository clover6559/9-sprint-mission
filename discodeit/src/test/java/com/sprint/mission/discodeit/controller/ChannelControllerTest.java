package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.request.CreatePublicChannelRequest;
import com.sprint.mission.discodeit.entity.Channel.ChannelType;
import com.sprint.mission.discodeit.exception.Channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.service.ChannelService;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ChannelController.class)
class ChannelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ChannelService channelService;

    @Test
    @DisplayName("공개 채널 생성 성공")
    void createPublic_success() throws Exception {
        CreatePublicChannelRequest request = new CreatePublicChannelRequest("General", "공개 채널 설명");
        ChannelDto responseDto = new ChannelDto(
                UUID.randomUUID(), ChannelType.PUBLIC, "General", "공개 채널 설명", Collections.emptyList(), Instant.now());

        given(channelService.create(any(CreatePublicChannelRequest.class))).willReturn(responseDto);

        mockMvc.perform(post("/api/channels/public")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("General"))
                .andExpect(jsonPath("$.description").value("공개 채널 설명"));
    }

    @Test
    @DisplayName("유저 ID 기반 채널 목록 조회 성공")
    void findAll_success() throws Exception {
        UUID userId = UUID.randomUUID();
        ChannelDto channel1 = new ChannelDto(
                UUID.randomUUID(), ChannelType.PUBLIC, "Channel 1", "Desc 1", Collections.emptyList(), Instant.now());
        given(channelService.findAllByUserId(userId)).willReturn(List.of(channel1));

        mockMvc.perform(get("/api/channels").param("userId", userId.toString()).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("Channel 1"));
    }

    @Test
    @DisplayName("채널 업데이트 실패 - 존재하지 않는 채널 ID")
    void update_fail_ChannelNotFound() throws Exception {
        UUID channelId = UUID.randomUUID();
        ChannelUpdateRequest request = new ChannelUpdateRequest("New Name", "New Description");
        given(channelService.update(eq(channelId), any(ChannelUpdateRequest.class)))
                .willThrow(new ChannelNotFoundException(channelId));

        mockMvc.perform(patch("/api/channels/{channelId}", channelId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("채널 삭제 성공")
    void delete_success() throws Exception {
        UUID channelId = UUID.randomUUID();

        mockMvc.perform(delete("/api/channels/{channelId}", channelId))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
