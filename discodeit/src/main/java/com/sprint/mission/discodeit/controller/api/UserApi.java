package com.sprint.mission.discodeit.controller.api;

import static com.sprint.mission.discodeit.controller.api.ApiDocsUtils.BAD_REQUEST_400;
import static com.sprint.mission.discodeit.controller.api.ApiDocsUtils.CREATED_201;
import static com.sprint.mission.discodeit.controller.api.ApiDocsUtils.NOT_FOUND_404;
import static com.sprint.mission.discodeit.controller.api.ApiDocsUtils.NO_CONTENT_204;
import static com.sprint.mission.discodeit.controller.api.ApiDocsUtils.SUCCESS_200;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.data.UserStatusDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "User", description = "User API")
public interface UserApi {

  @Operation(summary = "회원 등록")
  @ResponseStatus(HttpStatus.CREATED)
  @ApiResponses(
      value = {
          @ApiResponse(
              responseCode = CREATED_201,
              description = "User가 성공적으로 생성됨",
              content = @Content(schema = @Schema(implementation = User.class))),
          @ApiResponse(
              responseCode = BAD_REQUEST_400,
              description = "같은 email 또는 username를 사용하는 User가 이미 존재함",
              content =
              @Content(examples = @ExampleObject(value = "User with email {email} already exists"))),
      })
  ResponseEntity<UserDto> create(
      @Parameter(description = "User 생성 정보", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
      UserCreateRequest userCreateRequest,
      @Parameter(
          description = "User 프로필 이미지",
          content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
      MultipartFile profile);

  @Operation(summary = "User 정보 수정")
  @ResponseStatus(HttpStatus.OK)
  @ApiResponses(
      value = {
          @ApiResponse(
              responseCode = SUCCESS_200,
              description = "User 정보가 성공적으로 수정됨",
              content = @Content(schema = @Schema(implementation = User.class))),
          @ApiResponse(
              responseCode = NOT_FOUND_404,
              description = "User를 찾을 수 없음",
              content = @Content(examples = @ExampleObject("User with id {userId} not found"))),
          @ApiResponse(
              responseCode = BAD_REQUEST_400,
              description = "같은 email 또는 username를 사용하는 User가 이미 존재함",
              content = @Content(examples = @ExampleObject("user with email {newEmail} already exists")))
      })
  ResponseEntity<UserDto> update(
      @Parameter(description = "수정할 User ID") UUID userId,
      @Parameter(description = "수정할 User 정보") UserUpdateRequest userUpdateRequest,
      @Parameter(description = "수정할 User 프로필 이미지") MultipartFile profile);

  @Operation(summary = "User 삭제")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ApiResponses(
      value = {
          @ApiResponse(responseCode = NO_CONTENT_204, description = "User가 성공적으로 삭제됨"),
          @ApiResponse(
              responseCode = NOT_FOUND_404,
              description = "User를 찾을 수 없음",
              content = @Content(examples = @ExampleObject(value = "User with id {id} not found")))
      })
  ResponseEntity<Void> delete(@Parameter(description = "삭제할 User ID") UUID userId);

  @Operation(summary = "전체 User 목록 조회")
  @ResponseStatus(HttpStatus.OK)
  @ApiResponses(
      value = {
          @ApiResponse(
              responseCode = SUCCESS_200,
              description = "User 목록 조회 성공",
              content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDto.class))))
      })
  ResponseEntity<List<UserDto>> findAll();

  @Operation(summary = "User 온라인 상태 업데이트")
  @ResponseStatus(HttpStatus.OK)
  @ApiResponses(
      value = {
          @ApiResponse(
              responseCode = SUCCESS_200,
              description = "User 온라인 상태가 성공적으로 업데이트됨",
              content = @Content(schema = @Schema(implementation = UserStatus.class))),
          @ApiResponse(
              responseCode = NOT_FOUND_404,
              description = "해당 User의 UserStatus를 찾을 수 없음",
              content =
              @Content(
                  examples = @ExampleObject(value = "UserStatus with userId {userId} not found")))
      })
  ResponseEntity<UserStatusDto> statusUpdate(
      @Parameter(description = "상태를 변경할 User ID") UUID userId,
      @Parameter(description = "변경할 User 온라인 상태 정보") UserStatusUpdateRequest request);
}
