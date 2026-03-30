package com.sprint.mission.discodeit.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
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
public class UserApiIntegrationTest {

  @Autowired
  protected MockMvc mockMvc;

  @Autowired
  protected ObjectMapper objectMapper;
  @Autowired
  private UserRepository userRepository;

  @Test
  @DisplayName("새로운 사용자를 생성한다.")
  void createUser() throws Exception {
    // given
    UserCreateRequest request = new UserCreateRequest("루다", "luda@example.com", "password123");
    String requestJson = objectMapper.writeValueAsString(request);

    MockMultipartFile userPart = new MockMultipartFile(
        "userCreateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        requestJson.getBytes(StandardCharsets.UTF_8)
    );
    MockMultipartFile filePart = new MockMultipartFile(
        "profile",
        "profile.png",
        MediaType.IMAGE_PNG_VALUE,
        "test-image".getBytes()
    );
    // when & then
    mockMvc.perform(multipart("/api/users")
            .file(userPart)
            .file(filePart)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.email").value("luda@example.com"))
        .andExpect(jsonPath("$.username").value("루다"));
  }

  @Test
  @DisplayName("사용자 정보를 수정한다.")
  void updateUser() throws Exception {
    // given (먼저 사용자 하나를 저장해둬야겠지?)
    // saveUser() 같은 헬퍼 메서드나 Repository를 직접 써서 미리 데이터를 넣어둬.
    User user = userRepository.save(User.builder()
        .username("oldUser")
        .email("old-update@test.com") // 절대 안 겹치게!
        .password("password123")
        .build());
    UUID userId = user.getId();
    UserUpdateRequest request = new UserUpdateRequest("수정된이름", "", "");
    MockMultipartFile updatePart = new MockMultipartFile(
        "userUpdateRequest", // 컨트롤러의 @RequestPart 이름과 일치!
        "",
        MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsString(request).getBytes(StandardCharsets.UTF_8)
    );
    // when & then
    mockMvc.perform(multipart("/api/users/{userId}", userId)
            .file(updatePart)
            .with(requestProcessor -> {
              requestProcessor.setMethod("PATCH"); // PATCH로 변경하는 핵심 포인트!
              return requestProcessor;
            }))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.newUsername").value("수정된이름"));
  }

  @Test
  @DisplayName("사용자 목록을 조회한다.")
  void findAllUsers() throws Exception {
    // given (테스트용 데이터 2명 정도 생성)
    userRepository.save(
        User.builder().username("list-user-1").email("list1@test.com").password("password123")
            .build());
    userRepository.save(
        User.builder().username("list-user-2").email("list2@test.com").password("password123")
            .build());
    // when & then
    mockMvc.perform(get("/api/users")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2));
  }

  @Test
  @DisplayName("사용자를 삭제한다.")
  void deleteUser() throws Exception {
    // given
    User user = userRepository.save(User.builder()
        .username("delete-user")
        .email("delete-target@test.com")
        .password("password123")
        .build());
    UUID userId = user.getId();
    // when & then
    mockMvc.perform(delete("/api/users/{userId}", userId))
        .andExpect(status().isNoContent());
  }
}
