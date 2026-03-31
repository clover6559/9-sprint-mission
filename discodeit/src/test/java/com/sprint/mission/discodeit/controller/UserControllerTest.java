package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.api.controller.UserController;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private UserService userService;

  @MockitoBean
  private UserStatusService userStatusService;

  @Test
  @DisplayName("유저 생성 성공 - 멀티파트 요청 처리 확인")
  void create_Success() throws Exception {
    UserCreateRequest request = new UserCreateRequest("testUser", "test@example.com",
        "password123");
    UserDto responseDto = new UserDto(UUID.randomUUID(), "testUser", "test@example.com", null,
        false);

    MockMultipartFile requestPart = new MockMultipartFile(
        "userCreateRequest", "", MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(request));

    given(userService.create(any(UserCreateRequest.class), any(Optional.class)))
        .willReturn(responseDto);

    mockMvc.perform(
            multipart("/api/users").file(requestPart).contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.username").value("testUser"))
        .andExpect(jsonPath("$.email").value("test@example.com"));
  }

  @Test
  @DisplayName("유저 생성 실패 - 유효하지 않은 입력 값 (이메일 형식 오류)")
  void create_Fail_InvalidInput() throws Exception {
    UserCreateRequest invalidRequest = new UserCreateRequest("user", "not-an-email", "pwd");

    MockMultipartFile requestPart = new MockMultipartFile(
        "userCreateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(invalidRequest));

    mockMvc.perform(multipart("/api/users").file(requestPart)).andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("유저 단건 조회 성공 - JSON 응답 검증")
  void findById_Success() throws Exception {
    UUID userId = UUID.randomUUID();
    UserDto userDto = new UserDto(userId, "searchMe", "search@example.com", null, false);

    given(userService.findAll()).willReturn(java.util.List.of(userDto));

    mockMvc.perform(get("/api/users").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").exists())
        .andExpect(jsonPath("$[0].username").value("searchMe"));
  }

  @Test
  @DisplayName("유저 상태 업데이트 실패 - 잘못된 요청 (Validation 예외)")
  void statusUpdate_Fail_InvalidRequest() throws Exception {
    UUID userId = UUID.randomUUID();

    UserStatusUpdateRequest invalidRequest = new UserStatusUpdateRequest(null);

    mockMvc.perform(patch("/api/users/{userId}/userStatus", userId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidRequest)))
        .andDo(print())
        .andExpect(status().isBadRequest()); // @Valid에 의해 400 Bad Request 발생 검증
  }
}
