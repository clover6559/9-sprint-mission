package com.sprint.mission.discodeit.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.service.UserService;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
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

    @Autowired protected MockMvc mockMvc;

    @Autowired protected ObjectMapper objectMapper;

    @Autowired private UserService userService;

    @Test
    @DisplayName("새로운 사용자를 생성한다.")
    void createUser() throws Exception {
        // given
        UserCreateRequest request = new UserCreateRequest("루다", "luda@example.com", "password123");
        String requestJson = objectMapper.writeValueAsString(request);

        MockMultipartFile userPart =
                new MockMultipartFile(
                        "userCreateRequest",
                        "",
                        MediaType.APPLICATION_JSON_VALUE,
                        requestJson.getBytes(StandardCharsets.UTF_8));
        MockMultipartFile filePart =
                new MockMultipartFile(
                        "profile",
                        "profile.png",
                        MediaType.IMAGE_PNG_VALUE,
                        "test-image".getBytes());
        // when & then
        mockMvc.perform(
                        multipart("/api/users")
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
        // 1. Given: 이 테스트 전용 유니크 유저
        UserDto created =
                userService.create(
                        new UserCreateRequest(
                                "updateUser", "update-test@example.com", "password123"),
                        Optional.empty() // profile 없음
                        );
        UUID userId = created.id();

        UserUpdateRequest request =
                new UserUpdateRequest("fixedLuda", "fixed@example.com", "newpassword123");
        MockMultipartFile updatePart =
                new MockMultipartFile(
                        "userUpdateRequest",
                        "",
                        MediaType.APPLICATION_JSON_VALUE,
                        objectMapper.writeValueAsString(request).getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(
                        multipart("/api/users/{userId}", userId)
                                .file(updatePart)
                                .with(
                                        processor -> {
                                            processor.setMethod("PATCH");
                                            return processor;
                                        }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("fixedLuda"));
    }

    @Test
    @DisplayName("사용자 목록을 조회한다.")
    void findAllUsers() throws Exception {
        // Given: 다른 테스트와 절대 안 겹치는 데이터
        userService.create(
                new UserCreateRequest("listUser1", "list1@example.com", "password123"),
                Optional.empty());
        userService.create(
                new UserCreateRequest("listUser2", "list2@example.com", "password123"),
                Optional.empty());

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("사용자를 삭제한다.")
    void deleteUser() throws Exception {
        // Given: 삭제만을 위해 태어난 유저
        UserDto created =
                userService.create(
                        new UserCreateRequest(
                                "deleteUser", "delete-target@example.com", "password123"),
                        Optional.empty());
        UUID userId = created.id();

        mockMvc.perform(delete("/api/users/{userId}", userId)).andExpect(status().isNoContent());
    }
}
